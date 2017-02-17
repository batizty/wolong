package com.weibo.datasys

import akka.actor.{ActorContext, Props}
import com.weibo.datasys.rest.AuthService

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

  val scheduler = context.system.scheduler

  def receive: Receive = runRoute(authRoute) orElse {
    case m: Any =>
      log.error("Not Support Message :" + m.toString + " From Actor " + sender().toString())
  }
}