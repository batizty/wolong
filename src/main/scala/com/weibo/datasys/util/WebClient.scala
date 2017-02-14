package com.weibo.datasys.util

import akka.actor.ActorSystem
import akka.util.Timeout
import com.weibo.datasys.rest.Configuration
import org.slf4j.LoggerFactory
import spray.client.pipelining._
import spray.http.HttpResponse
import spray.httpx.unmarshalling.FromResponseUnmarshaller

import scala.concurrent.Future
import scala.concurrent.duration._

/**
 * Created by tuoyu on 04/02/2017.
 */
object WebClient extends Configuration {
  val log = LoggerFactory.getLogger(getClass.getName)
  implicit val system = ActorSystem(cluster_name)

  import system.dispatcher

  implicit val timeout = web_timeout seconds
  implicit val receiveTimeout: Timeout = web_timeout seconds

  def accessURL[T: FromResponseUnmarshaller: Manifest](url: String): Future[Option[T]] = {
    val pipeline = sendReceive ~> unmarshal[T]
    val l = "Access URL " + url
    pipeline {
      Get(url)
    } map {
      case tt: T => Some(tt)
      case msg: HttpResponse =>
        log.error(l + " got HttpResponse Result " + msg.message)
        None
      case err: Throwable =>
        log.error(l + " got Error : " + err.getMessage)
        None
      case m: Any =>
        log.error(l + "got UnRecognize Result " + m.toString)
        None
    }
  }
}
