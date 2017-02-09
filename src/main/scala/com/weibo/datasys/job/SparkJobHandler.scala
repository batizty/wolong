package com.weibo.datasys.job

import akka.actor.{ActorRef, PoisonPill, Props}
import akka.util.Timeout
import com.weibo.datasys.BaseActor
import com.weibo.datasys.job.data.{Job, JobStatus, SparkJob}
import com.weibo.datasys.job.mesos.MesosJobHandler

import scala.concurrent.duration._

/**
  * Created by tuoyu on 06/02/2017.
  */
trait BaseJobHandler
  extends BaseActor {
  // TODO
}

object SparkJobHandler {
  val Name = "Spark-Job-Handler"

  def props(): Props = Props(new SparkJobHandler())

}

class SparkJobHandler
  extends BaseJobHandler {

  import MesosJobHandler._

  // TODO 目前先使用定时任务来推动，后续需要改成mesos的消息来推动，mesos的offer来推动
  val scheduler = context.system.scheduler
  val refresh_time_interval = 5 minutes
  implicit val timeout: Timeout = 5 seconds


  val scriberActor = context.actorSelection("../" + JobManager.Name)

  /**
    * 这里的设计思路为
    * 给每个在运行的job都生成一个Actor，这个Actor会负责
    * 1 和自己的维护的Job进行数据交换
    * 2 在自己维护，对自己维护的Job进行控制管理
    * 3 如果有需求，可能需要做一部分采样
    */
  var jobActors: Map[String, ActorRef] = Map.empty

  override def preStart = {
    super.preStart()
    //    getAvailableJobList(None)
  }

  def receive = {
    //    case m: GetAvailableJob => {
    //      scriberActor ! m
    //    }
    //    case m: AvailableJobList => getAvailableJobList(Some(m))
    //
    //    case m: UpdateTaskStatus => {
    //      scriberActor ! ChangeJobStatus(m.job)
    //      updateActors(m)
    //    }
    case _ => ()
  }

//  def updateActors(m: UpdateTaskStatus): Unit = synchronized {
//    import JobStatus._
//    val job = m.job.asInstanceOf[SparkJob]
//    log.info(s"Update Job ${job.jobId} Status to ${job.jobStatus}")
//    job.jobStatus match {
//      case TaskFinished |
//           TaskFailed |
//           TaskKilled |
//           TaskLost |
//           TaskError =>
//        jobActors.get(job.jobId) foreach { actor =>
//          log.info(s"Job ${job.jobId} Running Task Over, So kill Monitor Actor")
//          actor ! PoisonPill
//          deleteJobActor(job)
//        }
//      case _ =>
//        if (jobActors.get(job.jobId).isEmpty) {
//          val jobActor = context.actorOf(MesosJobHandler.props(job), MesosJobHandler.getName(jobId = job.jobId))
//          addJobActor(job, jobActor)
//        }
//    }
//  }

  private def addJobActor(job: Job, actor: ActorRef): Unit = synchronized {
    jobActors += (job.jobId -> actor)
  }

  private def deleteJobActor(job: Job): Unit = synchronized {
    jobActors = jobActors - job.jobId
  }

  //
  //  def getAvailableJobList(msgOption: Option[AvailableJobList] = None): Unit = {
  //    log.info(s"GetAvailableJobList ${DateTime.now} and set scheduler again")
  //    scheduler.scheduleOnce(refresh_time_interval, self, GetAvailableJob(Some(JobType.SPARK)))
  //    msgOption foreach { msg =>
  //      getFirstSatisfyJob(msg.data, "TODO") foreach { job =>
  //        val jobActor = context.actorOf(MesosJobHandler.props(job), MesosJobHandler.getName(jobId = job.jobId))
  //        jobActors = jobActors + (job.jobId -> jobActor)
  //        (jobActor ? StartTask(job)) onComplete {
  //          case Success(init) if init.isInstanceOf[ActorInitOk] =>
  //            log.info(s"Init JobActor for Job ${job.jobId} Success")
  //            addJobActor(job, jobActor)
  //          case Failure(err) =>
  //            log.error(s"Init JobActor for Job ${job.jobId} Failed")
  //        }
  //      }
  //    }
  //    ()
  //  }

  /**
    * TODO 这里的resource 需要加上mesos的资源数据
    * 1 checkMesosResource
    * 2 checkGroup的限制条件
    * 3 checkHDFS的限制条件
    * 4 checkUserAndGroup
    *
    * @param jobList
    * @param resources
    * @return
    */
  def getFirstSatisfyJob(jobList: List[Job], resources: String): Option[SparkJob] = {
    jobList.headOption.map(_.asInstanceOf[SparkJob])
  }


}
