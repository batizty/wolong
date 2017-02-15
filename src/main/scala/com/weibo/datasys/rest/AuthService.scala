package com.weibo.datasys.rest

import akka.actor.{ActorSelection, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.weibo.datasys.job.JobManager
import com.weibo.datasys.job.data.SparkJob
import com.weibo.datasys.{JobSchedulerActor, Path => AuthServicePath, SecondPath => AuthServiceSecondPath}
import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s.native.Serialization
import spray.routing.{HttpService, StandardRoute}

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration._

/**
 * Created by tuoyu on 03/02/2017.
 */
trait AuthService
  extends HttpService
  with Configuration {

  implicit val formats = DefaultFormats
  implicit val timeout = Timeout(expiredTime seconds)

  val authWorker = actorRefFactory.actorOf(Props[AuthWorker], "auth-worker")

  val jobManager: ActorSelection = actorRefFactory.actorSelection(
    "akka.tcp://wolong@10.77.112.153:2552/user/" +
      JobSchedulerActor.Name + "/" + JobManager.Name
  )

  val authRoute = {
    pathPrefix(AuthServicePath.AUTH / AuthServicePath.CLUSTER) {
      path(AuthServiceSecondPath.HADOOP_POLICY_XML) {
        get {
          rejectEmptyResponse {
            sendToWorker(GetValidHadoopXML())
          }
        }
      } ~
        path(AuthServiceSecondPath.ADD_USER_SHELL) {
          get {
            sendToWorker(GetValidShell())
          }
        } ~
        path(AuthServiceSecondPath.AUTHORIZED_CHECK) {
          get {
            parameters("user_name", "user_group".?, "password".?, "token".?) {
              (un, ugO, pO, tO) => sendToWorker(CheckUserValid(un, ugO, pO, tO))
            }
          }
        }
    } ~
      path(AuthServicePath.SCHEDULER / AuthServiceSecondPath.SPARK_JOB) {
        post {
          entity(as[String]) { ss =>
            try {
              val job = parse(ss).extract[SparkJob]
              sendToScheduler(ss)
            } catch {
              case err: Throwable =>
                throw err
                //                log.error(err, "Extract SparkJob Failed with String : " + ss)
                complete(AuthResult().toString)
            }
          }
        }
      }
  }

  implicit def executionContext: ExecutionContextExecutor = actorRefFactory.dispatcher

  def sendToScheduler[T](job: String): StandardRoute = {
    implicit val timeout = Timeout(20 seconds)
    complete { (jobManager ? job).map(_.toString) }
  }

  def sendToWorker[T](msg: AuthMessage): StandardRoute = {
    complete {
      (authWorker ? msg) map { returnMsg =>
        returnMsg match {
          case m: ValidConfFile => m.message
          case m: AuthResult => m.toString
        }
      }
    }
  }
}

sealed trait AuthMessage

case class GetValidHadoopXML() extends AuthMessage

case class GetValidShell() extends AuthMessage

case class ValidConfFile(message: String) extends AuthMessage

case class CheckUserValid(
  name: String,
  group: Option[String] = None,
  password: Option[String] = None,
  token: Option[String] = None
) extends AuthMessage

case class AuthResult(message: String = "", code: Int = 0) extends AuthMessage {
  implicit val format = Serialization.formats(NoTypeHints)

  override def toString: String = {
    Serialization.writePretty(this)
  }

  def isAuthorizedUser: Boolean = code == 0
}

