package com.weibo.datasys

import akka.actor.Props
import com.weibo.datasys.job.JobManager

/**
 * TODO
 * 本Actor的主要任务
 * 1 启动JobManager
 * 2 对JobManager进行监控，在JobManager出现失败的时候，进行错误恢复
 * Created by tuoyu on 06/02/2017.
 */
object JobSchedulerActor {
  val Name = "job-scheduler"
  def props(): Props = Props(new JobSchedulerActor())
}

class JobSchedulerActor
    extends BaseActor {

  val jobManager = actorRefFactory.actorOf(JobManager.props(), JobManager.Name)

  def actorRefFactory = context

  def receive = {
    case _ => ()
  }
}
