package com.weibo.datasys.job.mesos

import akka.actor.{ActorRef, Props}
import com.weibo.datasys.BaseActor
import com.weibo.datasys.job.JobManager.ChangeJobStatus
import com.weibo.datasys.job.data.{Job, JobStatus, SparkJob}
import org.joda.time.DateTime

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by tuoyu on 08/02/2017.
  */
object MesosJobHandler {
  val Name = "MesosJobHandler"

  def getName(jobId: String): String = {
    Name + "_" + jobId + "_" + DateTime.now.getMillis
  }

  def props(job: Job): Props = Props(new MesosJobHandler(job))


  // 启动作业
  case class StartTask(job: Job)

  // 杀死作业
  case class KillTask()

  // 更新作业状态
//  case class UpdateTaskStatus(job: Job)

  case class ActorInitOk()

}

class MesosJobHandler(job: Job)
  extends BaseActor {

  import MesosJobHandler._

  val parent = context.parent
  var currentJob = job

  def receive = {
    case m: StartTask => startTask(sender(), job)
    case m: KillTask => killTask()
    case _ =>
  }

  private def killTask(): Unit = {
    // TODO kill作业
  }

  private def startTask(s: ActorRef, job: Job): Unit = {
    currentJob = job
    s ! ActorInitOk()
    val sj = job.asInstanceOf[SparkJob]
    MesosSimpleHandler.run(sj.toTask(), Some(job.jobUser)) { status =>
      val jobStatus: JobStatus.Value = status.state
      currentJob = sj.copy(
        status = jobStatus.id.toString,
        task_id = status.taskId.toString()
      )
      parent ! ChangeJobStatus(currentJob)
    } foreach { unit =>
      log.info(s"Job ${job} finished")
    }
  }

}
