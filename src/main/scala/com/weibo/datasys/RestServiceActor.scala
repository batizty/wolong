package com.weibo.datasys

import akka.actor.{Actor, ActorLogging}
import com.weibo.datasys.rest.AuthService


/**
  * Created by tuoyu on 03/02/2017.
  */

class RestServiceActor
  extends Actor
    with AuthService
    with ActorLogging {

  def actorRefFactory = context

  def receive = runRoute(authRoute)

}