package com.weibo.datasys

import com.weibo.datasys.job.{JobScriber, SparkJobHandler}

/**
  * Created by tuoyu on 06/02/2017.
  */
class JobSchedulerActor
  extends BaseActor {
  val jobScriber = actorRefFactory.actorOf(JobScriber.props(), JobScriber.Name)
  val sparkJobHandler = actorRefFactory.actorOf(SparkJobHandler.props(), SparkJobHandler.Name)

  def actorRefFactory = context

  def receive = {
    case _ => ()
  }
}
