package com.weibo.datasys

import akka.actor.{ActorContext, Props}
import com.weibo.datasys.RestServiceActor.SayHello
import com.weibo.datasys.job.JobManager
import com.weibo.datasys.rest.AuthService

import scala.concurrent.duration._

/**
 * Created by tuoyu on 03/02/2017.
 */

object RestServiceActor {
  val Name = "rest-service"

  def props(): Props = Props(new RestServiceActor())

  case class SayHello()

}

class RestServiceActor
  extends BaseActor
  with AuthService {

  def actorRefFactory: ActorContext = context

  override def preStart(): Unit = {
    scheduler.scheduleOnce(10 seconds, self, SayHello())
  }

  val remoteActor = context.actorSelection(
    "akka.tcp://wolong@10.77.112.153:2552/user/" +
      JobSchedulerActor.Name + "/" + JobManager.Name
  )

  val scheduler = context.system.scheduler

  def receive: Receive = runRoute(authRoute) orElse {
    case m: SayHello => {
      // TODO Testing Code
      log.info(s"Sendout hi to RemoteActor $remoteActor")
      remoteActor ! "hi"
      scheduler.scheduleOnce(10 seconds, self, SayHello())
    }
    case m: Any =>
      log.error("Not Support Message :" + m.toString + " From Actor " + sender().toString())

  }
}