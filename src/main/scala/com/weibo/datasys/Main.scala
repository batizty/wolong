package com.weibo.datasys

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import com.weibo.datasys.rest.Configuration
import spray.can.Http

/**
  * Created by tuoyu on 25/01/2017.
  */
object Main
  extends Configuration {

  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem(s"wolong-rest-service")

    startJobSchedulerService()
    //    startRestService()

    def startRestService() = {
      val restService = system.actorOf(Props[RestServiceActor], "rest-service")
      IO(Http) ! Http.Bind(restService, host, port)
    }

    def startJobSchedulerService() = {
      val jobSchedulerService = system.actorOf(Props[JobSchedulerActor], "scheduler-service")
    }
  }
}
