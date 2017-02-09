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

  implicit val system = ActorSystem("spray-client")

  import system.dispatcher

  implicit val timeout = web_timeout seconds
  implicit val receiveTimeout: Timeout = web_timeout seconds

  //  def main(args: Array[String]): Unit = {
  //    import org.json4s._
  //    import org.json4s.native.JsonMethods.parse
  //    implicit val formats = DefaultFormats
  //    case class XX(code: Int, data: List[WebGroup])
  //
  //    accessURL[String]("http://mlplat.intra.weibo.com/math/getGroup") foreach { str =>
  //      println(s"url result = $str")
  //      str foreach { ss =>
  //        val x = parse(ss).extract[XX]
  //        println(s"ss = $ss x = $x")
  //      }
  //
  //    }
  //  }

  def accessURL[T: FromResponseUnmarshaller: Manifest](url: String): Future[Option[T]] = {
    val pipeline = sendReceive ~> unmarshal[T]
    pipeline {
      Get(url)
    } map {
      case tt: T => Some(tt)
      case unexpect: HttpResponse =>
        log.error(s"Access URL $url got unexpect result ${unexpect.message}")
        None
      case err: Throwable =>
        log.error(s"Access URL $url got error : ${err.getMessage}")
        None
      case m =>
        log.error(s"Access URL $url got unexpect result ${m.toString}")
        None
    }
  }
}
