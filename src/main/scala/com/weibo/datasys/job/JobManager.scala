package com.weibo.datasys.job

import akka.actor.Props
import akka.util.Timeout
import com.nokia.mesos.DriverFactory
import com.nokia.mesos.api.stream.MesosEvents.TaskEvent
import com.weibo.datasys.BaseActor
import com.weibo.datasys.job.data.{ Job, JobStatus, SparkJob }
import com.weibo.datasys.job.mesos.WeiFrameworkFactory
import com.weibo.datasys.rest.Configuration
import org.apache.mesos.mesos.FrameworkInfo
import org.joda.time.DateTime
import org.json4s.DefaultFormats
import org.json4s.native.JsonMethods._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.util.{ Failure, Success }

import com.weibo.datasys.util.WebClient

/**
 * JobManager 作用
 * 1 定期从接口获得新的Job
 * 2 接受rest接口传递来的新Job
 * 3 根据Job的情况，启动JobActor来执行任务，并且对执行中的任务进行监控
 * 4 管理JobActor
 * 5 对接调度插件
 * 6 对接Mesos资源内容
 *
 * Created by tuoyu on 06/02/2017.
 */
object JobManager {
  val Name = "Job-Manager"

  def props(): Props = Props(new JobManager())

  case class RefreshJobList()

  case class ChangeJobStatus(job: Job)

  private[JobManager] case class AddJobs(jobs: List[Job])

  private[JobManager] case class DeleteJob(id: String)

  private case class XX(code: Int, data: List[SparkJob])

}

class JobManager
    extends BaseActor
    with SimpleSchedulerFIFO
    with Configuration {

  // imports all messages(case class)
  import JobManager._

  // all implicit value
  implicit val formats = DefaultFormats
  implicit val timeout: Timeout = 10 seconds

  // TODO 后续这些机制可能会有变化
  val scheduler = context.system.scheduler
  val refresh_time_interval = 600 seconds
  val _mesos_framework_info = FrameworkInfo(
    name = mesos_framework_name,
    user = mesos_default_user
  )
  val _mesos_driver = DriverFactory.createDriver(_mesos_framework_info, mesos_url)
  val _mesos_framework = WeiFrameworkFactory.createFramework(_mesos_driver)
  // private Value for Scheduler
  private var _jobMap: Map[String, Job] = Map.empty

  override def preStart = {
    super.preStart()

    // Init mesos FrameWork
    _mesos_framework.connect() onComplete {
      case Success((fwId, master, driver)) =>
        log.info(s"init fw = ${_mesos_framework}")
        log.info(s" detail fwId = $fwId master = $master, driver = $driver")
        scheduler.scheduleOnce(60 seconds, self, RefreshJobList())
      case Failure(err) =>
        logError(err, s"Init Mesos FrameWork failed")
        sys.exit(0)
    }
  }

  def receive = {
    case m: AddJobs => {
      _jobMap = _jobMap ++ m.jobs.map { job => (job.jobId, job) }.toMap
      log.info(s"jobMap = ${showJobMap}")
      reScheduleJobs()
    }
    case m: DeleteJob => {
      _jobMap -= m.id
      log.info(s"jobMap = ${showJobMap}")
      reScheduleJobs()
    }
    case m: RefreshJobList => refreshJobList()
    case m: ChangeJobStatus => {
      changeJobStatus(m)
      reportJobStatus(m)
    }

    case _ => ()
  }

  def reportJobStatus(m: ChangeJobStatus): Unit = {
    WebClient.accessURL[String](web_update_task_url.format(m.job.jobId, m.job.jobStatus.toString)) onComplete {
      case Success(result) =>
        log.info(s"Report Job Status to Web Front, and Result : $result")
      case Failure(err) =>
        logError(err, s"Report Job ${m.job.summary} to WebFront Error")
    }
  }

  /**
   * Get Job Data From Remote RestAPI and refresh _jobMap
   */
  def refreshJobList(): Unit = {
    log.info(s"RefreshJobList ${DateTime.now} and setting scheduler again")

    // send self to refreshJobList $refresh_time_interval min later
    scheduler.scheduleOnce(refresh_time_interval, self, RefreshJobList())

    log.debug(s"Before refreshJobList JobList = ${showJobMap}")

    WebClient.accessURL[String](web_task_url) map { ssOption =>
      log.debug(s"WebClient get newest Job List from ${web_task_url} with result ${ssOption}")
      ssOption map { ss =>
        try {
          parse(ss).extract[XX].data
        } catch {
          case err: Throwable =>
            logError(err, "Extract SparkJob failed")
            List.empty
        }
      } getOrElse List.empty
    } onComplete {
      case Success(taskList) =>
        updateJobMap(taskList)
      case Failure(err) =>
        logError(err, s"WebClient get newest Job List from ${web_task_url} Failed")
    }
  }

  /**
   * Update _jobMap
   *
   * @param taskList
   */
  def updateJobMap(taskList: List[SparkJob]): Unit = {
    val waitingJobList = taskList.filter(_.canScheduler)
    log.debug(s"Waiting Job List = ${waitingJobList.mkString("\n")}")
    if (waitingJobList.nonEmpty)
      self ! AddJobs(waitingJobList)
  }

  /**
   * show _jobMap details
   */
  def showJobMap: String = {
    val ss = for { (id, task) <- _jobMap } yield {
      s"$id -> ${task.summary}"
    }
    ss.mkString("\n")
  }

  def reScheduleJobs() = {
    getSatisfyJob(_jobMap.map(_._2).toList) foreach { job =>
      var currentJob = job.asInstanceOf[SparkJob]
      val task = currentJob.toTask()
      val launcher = _mesos_framework.submitTask(task)
      for { task <- launcher.info } {
        log.info(s"Submit ${currentJob.summary} to MesosFrameWork ${_mesos_framework_info.name}")
        currentJob = currentJob.copy(
          mesos_task_id = task.taskId.toString,
          status = JobStatus.TaskRunning.id.toString
        )
        self ! ChangeJobStatus(currentJob)
        launcher.events.subscribe(_ match {
          case te: TaskEvent =>
            log.info(s"Job ${currentJob.jobId} Status Change To ${te.status}")
            val jobStatus: JobStatus.Value = te.state
            currentJob = currentJob.copy(status = jobStatus.id.toString)
            self ! ChangeJobStatus(currentJob)
          case m =>
            log.error(s"Not Recognize Event $m")
        })
      }
    }
  }

  /**
   * ChangeJobStatus
   * 1 update Job Status depends jobid
   * 2 if job finished or failed status, delete job in _jobMap and _jobActor
   */
  def changeJobStatus(msg: ChangeJobStatus): Unit = synchronized {
    val job = msg.job
    log.info(s"Change Job ${job.jobId} Status To ${job.jobStatus}")
    // TODO report to FrontEnd Web
    if (job.isFinishedOrFailure) {
      self ! DeleteJob(job.jobId)
      //      self ! DeleteJobActor(job.jobId)
    } else {
      _jobMap.updated(job.jobId, job)
    }
  }

}
