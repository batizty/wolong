package com.weibo.datasys.rest

import akka.actor.ActorRef
import com.weibo.datasys.BaseActor
import com.weibo.datasys.rest.dao._
import com.weibo.datasys.rest.data.{Group, User}

import scala.concurrent.Future

/**
  * Created by tuoyu on 03/02/2017.
  */

class AuthWorker
  extends BaseActor
    with Configuration {

  import scala.concurrent.ExecutionContext.Implicits.global

  private val (userDao: UserDao, groupDao: GroupDao) =
    if (source == Configuration.DATA_SOURCE_DB)
      (new DBUserDao(), new DBGroupDao())
    else
      (new WebUserDao(), new WebGroupDao())

  def receive = {
    case m: GetValidHadoopXML => getHadoopXML(sender)
    case m: GetValidShell => getValidShell(sender)
    case m: CheckUserValid => checkUserValid(sender, m)
    case m => log.error("Not Recognize Message $m")
  }

  def getHadoopXML(s: ActorRef): Unit = {
    check(true, true) {
      case (us, gs) =>
        import util.HadoopPolicySettor
        HadoopPolicySettor.getValidHadoopPolicyXML(us, gs) foreach { xml =>
          s ! ValidConfFile(xml)
        }
    }
  }

  def getValidShell(s: ActorRef): Unit = {
    check(true, true) {
      case (us, gs) =>
        import util.HadoopShellSettor
        HadoopShellSettor.getValidHadoopShell(us, gs) foreach { sh =>
          s ! ValidConfFile(sh)
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

  def checkUserValid(s: ActorRef, msg: CheckUserValid): Unit = {
    userDao.getUserByName(msg.name) foreach { userOption =>
      val result = userOption match {
        case Some(user) if user.isValid => AuthResult()
        case _ => AuthResult(
          message = s"user ${msg.name} without this cluster privilege, Please contact to chenzhao1@staff.weibo.com to get information",
          code = 1
        )
      }
      s ! result
    }
  }
}
