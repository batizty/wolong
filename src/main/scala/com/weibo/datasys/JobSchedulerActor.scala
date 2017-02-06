package com.weibo.datasys

import akka.actor.Props
import com.weibo.datasys.job.JobScriber

/**
  * Created by tuoyu on 06/02/2017.
  */
class JobSchedulerActor
  extends BaseActor {

  val jobScriber = context.system.actorOf(Props[JobScriber], "job-scriber")

  def receive = {
    case _ => ()
  }
}
