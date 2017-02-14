package com.weibo.datasys

import java.io.File

import akka.actor.{ ActorSystem, Props }
import akka.io.IO
import com.typesafe.config.ConfigFactory
import com.weibo.datasys.rest.Configuration
import spray.can.Http

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
      val configFile = getClass.getClassLoader.getResource("rest.conf").getFile
      val config = ConfigFactory.parseFile(new File(configFile))
      implicit val system = ActorSystem(cluster_name, config)
      val restService = system.actorOf(Props[RestServiceActor], RestServiceActor.Name)
      IO(Http) ! Http.Bind(restService, host, port)
    }

    def startJobSchedulerService() = {
      val configFile = getClass.getClassLoader.getResource("scheduler.conf").getFile
      val config = ConfigFactory.parseFile(new File(configFile))
      implicit val system = ActorSystem(cluster_name, config)
      system.actorOf(JobSchedulerActor.props(), JobSchedulerActor.Name)
    }
  }
}
