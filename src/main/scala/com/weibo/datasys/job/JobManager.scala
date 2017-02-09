package com.weibo.datasys.job

import akka.actor.{ActorRef, PoisonPill, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.nokia.mesos.api.async.MesosException
import com.nokia.mesos.api.stream.MesosEvents.TaskEvent
import com.nokia.mesos.{FrameworkFactory, DriverFactory}
import com.weibo.datasys.BaseActor
import com.weibo.datasys.job.data.{JobStatus, Job, SparkJob}
import com.weibo.datasys.job.mesos.MesosJobHandler
import com.weibo.datasys.job.mesos.MesosJobHandler.{ActorInitOk, StartTask}
import com.weibo.datasys.job.mesos.MesosSimpleHandler._
import com.weibo.datasys.rest.Configuration
import org.apache.mesos.mesos.FrameworkInfo
import org.joda.time.DateTime
import org.json4s.DefaultFormats
import org.json4s.native.JsonMethods._

import scala.concurrent.ExecutionContext.Implicits.global
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

  private[JobManager] case class AddJobActor(id: String, actor: ActorRef)

  private[JobManager] case class DeleteJobActor(id: String)

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
  // 现在
  val scheduler = context.system.scheduler
  val refresh_time_interval = 30 seconds
  val avaliable_job_list_sz: Int = 5
  // private Value for Scheduler
  private var _jobMap: Map[String, Job] = Map.empty
  private var _jobActors: Map[String, ActorRef] = Map.empty

  val fw_info = FrameworkInfo(
    name = mesos_framework_name,
    user = mesos_default_user)

  val mk_dirver = DriverFactory.createDriver(fw_info, mesos_url)
  val fw = FrameworkFactory.createFramework(mk_dirver)

  override def preStart = {
    super.preStart()
    // After Actor init, send to self to refreshJobList 1min later
    fw.connect() foreach { case ((fwId, master, driver)) =>
      log.info(s"init fw = $fw")
      log.info(s" detail fwId = $fwId master = $master, driver = $driver")

      scheduler.scheduleOnce(10 seconds, self, RefreshJobList())
    }
  }


  def receive = {
    case m: AddJobs => {
      _jobMap = _jobMap ++ m.jobs.map { job => (job.jobId, job) }.toMap
    }
      log.info(s"jobMap = ${showJobMap}")
    case m: DeleteJob => {
      _jobMap -= m.id
    }
      log.info(s"jobMap = ${showJobMap}")
    case m: AddJobActor => {
      _jobActors += (m.id -> m.actor)
    }
      log.info(s"jobActors = ${_jobActors}")
    case m: DeleteJobActor => {
      _jobActors.get(m.id) foreach { actor =>
        actor ! PoisonPill
        _jobActors -= m.id
      }
    }

    case m: RefreshJobList => refreshJobList()
    case m: ChangeJobStatus => changeJobStatus(m)

    case _ => ()
  }

  /**
    * Get Job Data From Remote RestAPI and refresh _jobMap
    */
  def refreshJobList(): Unit = {
    import com.weibo.datasys.util.WebClient
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
        reScheduleJobs()
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
    val fList = taskList.filter(_.canScheduler)
    log.debug(s"Flist = $fList")
    if (fList.nonEmpty)
      self ! AddJobs(fList)
  }

  def reScheduleJobs() = {
    getSatisfyJob(_jobMap.map(_._2).toList) foreach { job =>
      var cj = job.asInstanceOf[SparkJob]
      val tinfo = cj.toTask()
      val lauched = fw.submitTask(tinfo)
      for {
        task <- lauched.info
      } {
        log.info(s"Submit Job $job")
        val taskId = task.taskId.toString
        cj = cj.copy(task_id = taskId, status = JobStatus.TaskRunning.id.toString)
        self ! ChangeJobStatus(cj)
        lauched.events.subscribe(x =>
          x match {
            case te: TaskEvent =>
              log.info(s" Task Event = $te")
              val jobStatus: JobStatus.Value = te.state
              cj = cj.copy(status = jobStatus.id.toString,
                task_id = te.taskId.toString()
              )
              self ! ChangeJobStatus(cj)
            case m =>
              log.debug(s"problem error m = $m")
          })
      }
    }
    //      launched = fw.submitTask(task)
    //      task <- launched.info
    //    } {
    //      log.info(s"Task successfully started on slave ${task.slaveId.value}")
    //      val taskId = task.taskId.toString
    //      launched.events.subscribe(_ match {
    //        case te: TaskEvent =>
    //          if (te.state.isTaskError)
    //            p.failure(new MesosException(s"Task Running Error ${te.toString}"))
    //          log.info(s" Task Event = $te")
    //          updateTaskStatus(te.status)
    //        case m =>
    //          log.debug(s"Mesos Running Event Not Support Now $m")
    //      })
    //      val jobActor = context.actorOf(
    //        MesosJobHandler.props(job),
    //        MesosJobHandler.getName(jobId = job.jobId)
    //      )
    //      (jobActor ? StartTask(job)) onComplete {
    //        case Success(init) if init.isInstanceOf[ActorInitOk] =>
    //          log.info(s"Init JobActor for Job ${job.jobId} Success")
    //          self ! AddJobActor(job.jobId, jobActor)
    //        case Failure(err) =>
    //          logError(err, s"Init JobActor for Job ${job.jobId} Failed")
    //      }
    //    }
  }

  /**
    * show _jobMap details
    */
  def showJobMap: String = {
    val ss = for {(id, task) <- _jobMap} yield {
      s"$id -> ${task.summary}"
    }
    ss.mkString("\n")
  }

  //  /**
  //    * TODO  这里后续需要修改算法
  //    * 1 具体是返回1个还是若干个？
  //    * 2 具体一次调度一个还是调度若干个？
  //    *
  //    * @param s
  //    * @param msg
  //    */
  //  def returnAvailableJob(s: ActorRef, msg: GetAvailableJob): Unit = {
  //    val jobs: List[Job] = jobMap.filter {
  //      case (id, task) =>
  //        task.jobStatus != JobStatus.TaskFinished &&
  //          task.jobStatus != JobStatus.TaskFailed &&
  //          task.jobStatus != JobStatus.TaskNotSupport &&
  //          task.jobStatus != JobStatus.TaskRunning
  //    }.filter {
  //      case (id, task) =>
  //        if (msg.typ.isDefined) msg.typ.exists(_ == task.jobType)
  //        else true
  //    }.map(_._2)
  //      .toList
  //      .sortBy(_.jobSubmitTime.getMillis)
  //      .take(avaliable_job_list_sz)
  //    s ! AvailableJobList(jobs)
  //    ()
  //  }

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
