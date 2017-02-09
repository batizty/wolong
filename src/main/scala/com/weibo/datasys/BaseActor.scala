package com.weibo.datasys

import akka.actor.{Actor, ActorLogging}

/**
  * Created by tuoyu on 06/02/2017.
  */
trait BaseActor
  extends Actor
    with ActorLogging {
  override def preStart = {
    log.info("Start Acotr " + getClass.getName)
  }

  override def postStop = {
    log.info("Stop Actor " + getClass.getName)
  }

  def logError(err: Throwable, s: String) = {
    log.error(err, s + s" with Error Message : ${err.getMessage}")
  }
}
