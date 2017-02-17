package com.weibo.datasys

import akka.actor.{Actor, ActorLogging}
import org.slf4j.LoggerFactory

/**
 * Created by tuoyu on 06/02/2017.
 */
trait BaseActor
  extends Actor
  with ActorLogging {

  override def preStart(): Unit = {
    log.info("Start Acotr " + getClass.getName)
  }

  override def postStop(): Unit = {
    log.info("Stop Actor " + getClass.getName)
  }

  def logError(err: Throwable, s: String): Unit = {
    log.error(err, s + s" with Error Message : ${err.getMessage}")
  }
}
