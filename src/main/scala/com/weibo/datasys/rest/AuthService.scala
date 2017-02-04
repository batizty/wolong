package com.weibo.datasys.rest

import akka.actor.Props
import akka.pattern.ask
import akka.util.Timeout
import com.weibo.datasys.{Path => AuthServicePath, SecondPath => AuthServiceSecondPath}
import spray.routing.HttpService

import scala.concurrent.duration._
import scala.util.{Failure, Success}

/**
  * Created by tuoyu on 03/02/2017.
  */
trait AuthService
  extends HttpService
    with Configuration {
  implicit def executionContext = actorRefFactory.dispatcher

  implicit val timeout = Timeout(expiredTime seconds)

  val authWorker = actorRefFactory.actorOf(Props[AuthWorker], "auth-worker")

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
            sendToWorker(CheckUserValid("tuoyu"))
          }
        }

    }
  }

  def sendToWorker[T](msg: AuthMessage) = {
    complete {
      (authWorker ? msg)
        .mapTo[AuthOK]
        .map { ok => ok.result }
    }
  }
}

sealed trait AuthMessage

case class GetValidHadoopXML() extends AuthMessage

case class GetValidShell() extends AuthMessage

case class CheckUserValid(
                           name: String,
                           group: Option[String] = None,
                           password: Option[String] = None,
                           token: Option[String] = None) extends AuthMessage

case class AuthOK(result: String) extends AuthMessage
