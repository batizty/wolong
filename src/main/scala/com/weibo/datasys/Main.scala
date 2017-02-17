package com.weibo.datasys

import akka.actor.{ ActorSystem, Props, _ }
import akka.io.IO
import akka.util.Timeout
import com.weibo.datasys.rest.Configuration
import spray.can.Http
import scala.concurrent.duration._

/**
 * Created by tuoyu on 25/01/2017.
 */
object Main
    extends Configuration {

  def main(args: Array[String]): Unit = {
    lazy val cmd = new ArgumentConf(args)

    if (cmd.help()) {
      cmd.printHelp()
      sys.exit(0)
    }

    if (cmd.rest_service()) {
      startRestService()
    } else if (cmd.scheduler_service()) {
      startJobSchedulerService()
    }

    def startRestService() = {
      implicit val system = ActorSystem(
        cluster_name,
        config.getConfig("rest-service").withFallback(config)
      )
      implicit val executionContext = system.dispatcher
      implicit val timeout = Timeout(10 seconds)

      val restService = system.actorOf(Props[RestServiceActor], RestServiceActor.Name)
      IO(Http) ! Http.Bind(restService, host, port)
    }

    def startJobSchedulerService() = {
      implicit val system = ActorSystem(
        cluster_name,
        config.getConfig("scheduler-service").withFallback(config)
      )
      system.actorOf(JobSchedulerActor.props(), JobSchedulerActor.Name)
    }
  }
}
