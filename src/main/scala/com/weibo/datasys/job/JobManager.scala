package com.weibo.datasys.job

import akka.actor.{Actor, Props}
import akka.util.Timeout
import com.nokia.mesos.DriverFactory
import com.nokia.mesos.api.stream.MesosEvents.TaskEvent
import com.weibo.datasys.job.data.{SparkJob2, Job, JobStatus, SparkJob}
import com.weibo.datasys.job.mesos.WeiFrameworkFactory
import com.weibo.datasys.rest.{AuthResult, Configuration}
import com.weibo.datasys.util.WebClient
import com.weibo.datasys.{BaseActor, RestServiceActor}
import org.apache.mesos.mesos.FrameworkInfo
import org.joda.time.DateTime
import org.json4s.DefaultFormats
import org.json4s.native.JsonMethods._

import scala.concurrent.duration._
import scala.util.{Failure, Success}

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
  implicit val executionContext = context.system.dispatcher

  val scheduler = context.system.scheduler
  val refresh_time_interval = 600 seconds
  val _mesos_framework_info = FrameworkInfo(
    name = mesos_framework_name,
    user = mesos_default_user
  )
  val _mesos_driver = DriverFactory.createDriver(_mesos_framework_info, mesos_url)
  val _mesos_framework = WeiFrameworkFactory.createFramework(_mesos_driver)

  val _auth_actor = context.actorSelection(
    "akka.tcp://"
      + cluster_name + "@10.212.250.102:2552/user/"
      + RestServiceActor.Name
  )

  // private Value for Scheduler
  private var _jobMap: Map[String, Job] = Map.empty

  override def preStart(): Unit = {
    super.preStart()

    val _first_fresh_time_interval = 60 seconds

    // Init mesos FrameWork
    _mesos_framework.connect() onComplete {
      case Success((fwId, master, driver)) =>
        log.info("Init Mesos Frame Work " + _mesos_framework)
        log.info("Detail Frame Work  Id = " + fwId + ", Master = " + master + ", Driver = " + driver)
        scheduler.scheduleOnce(
          _first_fresh_time_interval,
          self,
          RefreshJobList()
        )
      case Failure(err) =>
        logError(err, s"Init Mesos FrameWork Failed")
        sys.exit(0)
    }
  }

  override def postStop(): Unit = {
    super.postStop()
  }

  def receive: Actor.Receive = {
    case ss: String => {
      try {
        val job = parse(ss).extract[SparkJob2]
        self ! AddJobs(List(job))
        sender() ! AuthResult()
      } catch {
        case err: Throwable =>
          log.error(err, "Extract SparkJob Failed with String : " + ss)
          sender() ! AuthResult("Extract SparkJob Failed with String " + ss, 1)
      }

    }

    case m: AddJobs => {
      _jobMap = _jobMap ++ m.jobs.map { job => (job.jobId, job) }.toMap
      log.debug("jobMap = " + showJobMap)
      reScheduleJobs()
    }
    case m: DeleteJob => {
      _jobMap -= m.id
      log.debug("jobMap = " + showJobMap)
      reScheduleJobs()
    }
    case m: RefreshJobList => refreshJobList()
    case m: ChangeJobStatus => {
      changeJobStatus(m)
      reportJobStatus(m)
    }

    case m: Any =>
      log.error("Not Support Message :" + m.toString + " From Actor " + sender().toString())
  }

  def reportJobStatus(m: ChangeJobStatus): Unit = {
    val url = web_update_task_url.format(m.job.jobId, m.job.jobStatus.id)
    WebClient.accessURL[String](url) onComplete {
      case Success(result) =>
        log.info("Report Job Status to Web Front " + url + ", and Result : " + result)
      case Failure(err) =>
        logError(err, "Report Job " + m.job.summary + " to WebFront " + url + " Error")
    }
  }

  /**
   * Get Job Data From Remote RestAPI and refresh _jobMap
   */
  def refreshJobList(): Unit = {
    log.info(s"RefreshJobList ${DateTime.now} and setting scheduler again")

    // send self to refreshJobList $refresh_time_interval min later
    scheduler.scheduleOnce(refresh_time_interval, self, RefreshJobList())

    log.debug("Before refreshJobList JobList = " + showJobMap)

    WebClient.accessURL[String](web_task_url) map { ssOption =>
      log.debug("WebClient get newest Job List from " + web_task_url + " with result " + ssOption)
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
    log.debug("Waiting Job List = " + waitingJobList.mkString("\n"))
    if (waitingJobList.nonEmpty) {
      self ! AddJobs(waitingJobList)
    }
  }

  /**
   * show _jobMap details
   */
  def showJobMap: String = {
    val ss = for { (id, task) <- _jobMap } yield {
      id + " -> " + task.summary
    }
    ss.mkString("\n")
  }

  def reScheduleJobs(): Unit = {
    getSatisfyJob(_jobMap.map(_._2).toList) foreach { job =>
      var currentJob = job.asInstanceOf[SparkJob]
      val task = currentJob.toTask()
      val launcher = _mesos_framework.submitTask(task)
      for { task <- launcher.info } {
        log.info("Submit " + currentJob.summary + "to MesosFrameWork " + _mesos_framework_info.name)
        currentJob = currentJob.copy(
          mesos_task_id = task.taskId.toString,
          status = JobStatus.TaskRunning.id.toString
        )
        self ! ChangeJobStatus(currentJob)
        launcher.events.subscribe(taskEvent => taskEvent match {
          case te: TaskEvent =>
            val jobStatus: JobStatus.Value = te.state
            currentJob = currentJob.copy(status = jobStatus.id.toString)
            log.info("Job " + currentJob.jobId + "Status Change To " + currentJob.jobStatus)
            self ! ChangeJobStatus(currentJob)
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
    log.info("Change Job " + job.jobId + " Status To " + job.jobStatus)
    if (job.isFinishedOrFailure) {
      self ! DeleteJob(job.jobId)
    } else {
      _jobMap.updated(job.jobId, job)
    }
  }

}
