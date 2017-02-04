package com.weibo.datasys.rest

import akka.actor.{ActorRef, Actor, ActorLogging}

import com.weibo.datasys.rest.dao._
import com.weibo.datasys.rest.data.{User, Group}
import com.weibo.datasys.rest.util.HadoopPolicySettor

import scala.concurrent.Future

/**
  * Created by tuoyu on 03/02/2017.
  */

trait AuthOperations {
  def checkUserValid(): String = "x"
}


class AuthWorker
  extends Actor
    with AuthOperations
    with ActorLogging
    with Configuration {

  import scala.concurrent.ExecutionContext.Implicits.global

  override def preStart() = {
    log.info("AuthWorker Start")
  }

  private val (userDao: UserDao, groupDao: GroupDao) =
    if (source == Configuration.DATA_SOURCE_DB)
      (new DBUserDao(), new DBGroupDao())
    else
      (new WebUserDao(), new WebGroupDao())


  def receive = {
    case m: GetValidHadoopXML => getHadoopXML(sender)
    case m: GetValidShell => getValidShell(sender)
    case m: CheckUserValid => sender ! AuthOK(checkUserValid())
    case m => log.error("Not Recognize Message $m")
  }

  def getHadoopXML(s: ActorRef): Unit = {
    check(true, true) {
      case (us, gs) =>
        import util.HadoopPolicySettor
        HadoopPolicySettor.getValidHadoopPolicyXML(us, gs) foreach { xml =>
          s ! AuthOK(xml)
        }
    }
  }

  def getValidShell(s: ActorRef): Unit = {
    check(true, true) {
      case (us, gs) =>
        import util.HadoopShellSettor
        HadoopShellSettor.getValidHadoopShell(us, gs) foreach { sh =>
          s ! AuthOK(sh)
        }
    }
  }

  def check(
             userFlag: Boolean = false,
             groupFlag: Boolean = false
           )(f: (List[User], List[Group]) => Unit): Unit = {
    val users =
      if (userFlag)
        userDao.getAllUser()
      else
        Future(List.empty)
    val groups =
      if (groupFlag)
        groupDao.getAllGroup()
      else
        Future(List.empty)

    for {
      us <- users
      gs <- groups
    } {
      if (false == ((userFlag && us.isEmpty) || (groupFlag && gs.isEmpty))) {
        f(us, gs)
      }
    }
  }
}
