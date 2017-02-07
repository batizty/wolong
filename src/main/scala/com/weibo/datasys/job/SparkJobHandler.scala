package com.weibo.datasys.job

import akka.actor.Props
import com.weibo.datasys.BaseActor
import com.weibo.datasys.job.data.{Job, JobStatus, JobType, SparkJob}
import org.joda.time.DateTime

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

  import JobScriber._

  import scala.concurrent.ExecutionContext.Implicits.global

  // TODO 目前先使用定时任务来推动，后续需要改成mesos的消息来推动，mesos的offer来推动
  val scheduler = context.system.scheduler
  val refresh_time_interval = 5 minutes

  val scriberActor = context.actorSelection("../" + JobScriber.Name)

  override def preStart = {
    super.preStart()
    getAvailableJobList(None)
  }

  def receive = {
    case m: GetAvailableJob => scriberActor ! m
    case m: AvailableJobList => getAvailableJobList(Some(m))
    case _ => ()
  }

  def getAvailableJobList(msgOption: Option[AvailableJobList] = None): Unit = {
    log.info(s"GetAvailableJobList ${DateTime.now} and set scheduler again")
    scheduler.scheduleOnce(refresh_time_interval, self, GetAvailableJob(Some(JobType.SPARK)))
    msgOption foreach { msg =>
      getFirstSatisfyJob(msg.data, "TODO") foreach { job =>
        // TODO 修改这里的状态，直接和Task对接
        import com.weibo.datasys.job.mesos.MesosSimpleHandler
        MesosSimpleHandler.run(job.toTask(), None) { taskId =>
          scriberActor ! ChangeJobStatus(job.jobId, Some(taskId), JobStatus.RUNNING)
        } {
          scriberActor ! ChangeJobStatus(job.jobId, None, JobStatus.FINISHED)
          ()
        } {
          scriberActor ! ChangeJobStatus(job.jobId, None, JobStatus.KILLED)
          ()
        } foreach { xx =>
          log.info(s"Job ${job} finished")
        }
      }
    }
    ()
  }

  /**
    * TODO 这里的resource 需要加上mesos的资源数据
    *
    * @param jobList
    * @param resources
    * @return
    */
  def getFirstSatisfyJob(jobList: List[Job], resources: String): Option[SparkJob] = {
    jobList.headOption.map(_.asInstanceOf[SparkJob])
  }


}
