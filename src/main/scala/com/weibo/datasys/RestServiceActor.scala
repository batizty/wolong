package com.weibo.datasys

import akka.actor.{ ActorContext, Props }
import com.weibo.datasys.rest.AuthService

/**
 * Created by tuoyu on 03/02/2017.
 */

object RestServiceActor {
  val Name = "rest-service"

  def props(): Props = Props(new RestServiceActor())
}

class RestServiceActor
    extends BaseActor
    with AuthService {

  def actorRefFactory: ActorContext = context

  def receive: Receive = runRoute(authRoute)
}