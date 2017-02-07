package com.weibo.datasys.job

import akka.actor.{ActorRef, Props}
import com.weibo.datasys.BaseActor
import com.weibo.datasys.job.data.{Job, JobStatus, JobType, SparkJob}
import com.weibo.datasys.rest.Configuration
import org.joda.time.DateTime
import org.json4s.DefaultFormats
import org.json4s.native.JsonMethods._

import scala.concurrent.duration._
import scala.util.{Failure, Success}

/**
  * Created by tuoyu on 06/02/2017.
  * 从接口中获得所有Job信息
  */
object JobScriber {
  val Name = "Job-Scriber"

  def props(): Props = Props(new JobScriber())

  case class RefreshJobList()

  case class GetAvailableJob(typ: Option[JobType.Value] = None)

  case class ChangeJobStatus(id: String, taskId: Option[String] = None, status: JobStatus.Value)

  case class AvailableJobList(data: List[Job])

  case class XX(code: Int, data: List[SparkJob])

}


class JobScriber
  extends BaseActor
    with Configuration {

  import JobScriber._

  implicit val formats = DefaultFormats

  import scala.concurrent.ExecutionContext.Implicits.global

  val scheduler = context.system.scheduler
  val refresh_time_interval = 5 minutes
  val avaliable_job_list_sz: Int = 5
  private var jobMap: Map[String, Job] = Map.empty

  override def preStart = {
    super.preStart()
    refreshJobList()
  }

  def receive = {
    case m: RefreshJobList => refreshJobList()
    case m: GetAvailableJob => returnAvailableJob(sender, m)
    case m: ChangeJobStatus => changeJobStatus(m)
    case _ => ()
  }

  /**
    * TODO  这里后续需要修改算法
    * 1 具体是返回1个还是若干个？
    * 2 具体一次调度一个还是调度若干个？
    *
    * @param s
    * @param msg
    */
  def returnAvailableJob(s: ActorRef, msg: GetAvailableJob): Unit = {
    val jobs: List[Job] = jobMap.filter {
      case (id, task) =>
        task.jobStatus != JobStatus.FINISHED &&
          task.jobStatus != JobStatus.FAILED &&
          task.jobStatus != JobStatus.NOT_SUPPORT &&
          task.jobStatus != JobStatus.RUNNING
    }.filter {
      case (id, task) =>
        if (msg.typ.isDefined) msg.typ.exists(_ == task.jobType)
        else true
    }.map(_._2)
      .toList
      .sortBy(_.jobSubmitTime.getMillis)
      .take(avaliable_job_list_sz)
    s ! AvailableJobList(jobs)
    ()
  }

  def changeJobStatus(msg: ChangeJobStatus): Unit = synchronized {
    jobMap.get(msg.id) match {
      case Some(task) =>
        import SparkJob._
        val newTask = task match {
          case sj: SparkJob => sj.copy(status = msg.status)
          case _ => task
        }
        if (task.jobStatus != msg.status) {
          log.info(s"Change Job ${msg.id} Status From ${task.jobStatus} To ${msg.status}")
          jobMap = jobMap.updated(msg.id, newTask)
        }
      case None =>
        log.error(s"Could not find Job with id ${msg.id}")
    }
  }

  def refreshJobList(): Unit = {
    import com.weibo.datasys.util.WebClient

    log.info(s"RefreshJobList ${DateTime.now} and setting scheduler again")
    scheduler.scheduleOnce(refresh_time_interval, self, RefreshJobList())
    log.debug(s"Before refreshJobList JobList = ${showJobMap}")
    WebClient.accessURL[String](web_task_url) map { ssOption =>
      log.debug(s"ssOption = $ssOption")
      ssOption map { ss =>
        try {
          parse(ss).extract[XX].data
        } catch {
          case err: Throwable =>
            log.error(s"Extract SparkJob failed with Message : ${err.getMessage}")
            err.printStackTrace()
            List.empty
        }
      } getOrElse List.empty
    } onComplete {
      case Success(taskList) =>
        updateJobMap(taskList)
      case Failure(err) =>
        log.error("")
    }
    log.debug(s"After refreshJobList JobList = ${showJobMap}")
  }

  def updateJobMap(taskList: List[SparkJob]): Unit = synchronized {
    jobMap = (jobMap ++ taskList.map { task => (task.jobId, task) }.toMap)
  }

  def showJobMap: String = {
    val ss = for { (id, task) <- jobMap } yield {
      s"$id -> ${task.summary}"
    }
    ss.mkString("\n")
  }
}
