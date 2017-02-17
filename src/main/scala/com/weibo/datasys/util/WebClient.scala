package com.weibo.datasys.util

import akka.actor.ActorSystem
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import com.weibo.datasys.job.data.SparkJob
import com.weibo.datasys.rest.Configuration
import org.json4s.DefaultFormats
import org.slf4j.LoggerFactory
import spray.client.pipelining._
import spray.http.HttpResponse
import spray.httpx.unmarshalling.FromResponseUnmarshaller

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.{ Success, Failure }
import org.json4s.native.JsonMethods._

/**
 * Created by tuoyu on 04/02/2017.
 */
object WebClient extends Configuration {
  val log = LoggerFactory.getLogger(getClass.getName)
  val conf = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + 2553)
  implicit val system = ActorSystem(cluster_name, conf)

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

//  def main(args: Array[String]): Unit = {
//    implicit val formats = DefaultFormats
//    case class XX(code: Int, data: List[SparkJob])
//    accessURL[String]("http://mlplat.intra.weibo.com:8083/math/getTask?status=TaskStaging") map { ssOption =>
//      log.debug("WebClient get newest Job List from " + web_task_url + " with result " + ssOption)
//      ssOption map { ss =>
//        try {
//          parse(ss).extract[XX].data
//        } catch {
//          case err: Throwable =>
//            println("Extract SparkJob failed : " + err.toString)
//            List.empty
//        }
//      } getOrElse List.empty
//    } onComplete {
//      case Success(taskList) =>
//        taskList  foreach { task =>
//          println(s" ${task.summary}")
//          println(s" ${task.getShellCommand}")
//
//        }
//        println(s"taskLit = $taskList")
//      case Failure(err) =>
//        println(s"WebClient get newest Job List from ${web_task_url} Failed err = " + err)
//    }
//
//    ()
//  }
}
