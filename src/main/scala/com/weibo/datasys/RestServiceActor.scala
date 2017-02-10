package com.weibo.datasys

import com.weibo.datasys.rest.AuthService

/**
 * Created by tuoyu on 03/02/2017.
 */

class RestServiceActor
    extends BaseActor
    with AuthService {

  def actorRefFactory = context

  def receive = runRoute(authRoute)

}