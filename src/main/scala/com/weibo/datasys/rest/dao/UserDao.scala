package com.weibo.datasys.rest.dao

import com.weibo.datasys.rest.Configuration
import com.weibo.datasys.rest.data.{DBUserTable, User, WebUser}
import org.json4s._
import org.json4s.native.JsonMethods.parse
import org.slf4j.LoggerFactory
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * Created by tuoyu on 26/01/2017.
 */
trait UserDao {
  def getAllUser(): Future[List[User]]

  def getUserByName(name: String): Future[Option[User]]

  def getUserById(id: Long): Future[Option[User]]
}

class DBUserDao
  extends UserDao
  with Configuration {

  val log = LoggerFactory.getLogger("DBUserDao")
  val users = TableQuery[DBUserTable]
  private val db = Database.forURL(
    url = db_url,
    user = db_user,
    password = db_passwd,
    driver = db_driver
  )

  override def getAllUser(): Future[List[User]] = {
    db.run(users.result.map(_.toList))
  }

  override def getUserByName(name: String): Future[Option[User]] = {
    db.run(users.filter(_.name === name).result.headOption)
  }

  override def getUserById(id: Long): Future[Option[User]] = {
    db.run(users.filter(_.id === id).result.headOption)
  }
}

class WebUserDao
  extends UserDao
  with Configuration {
  val log = LoggerFactory.getLogger(getClass.getName)
  implicit val formats = DefaultFormats

  override def getUserByName(name: String): Future[Option[User]] = {
    getAllUser() map { us =>
      us.filter(_.name == name).headOption
    }
  }

  override def getAllUser(): Future[List[User]] = {
    import com.weibo.datasys.util.WebClient
    WebClient.accessURL[String](web_user_url) map { ssOption =>
      ssOption map { ss =>
        try {
          parse(ss).extract[XX].data
        } catch {
          case err: Throwable =>
            log.error(s"Extract WebUserDao failed with Message : ${err.getMessage}")
            List.empty
        }
      } getOrElse List.empty
    }
  }

  override def getUserById(id: Long): Future[Option[User]] = {
    getAllUser() map { us =>
      us.filter(_.id == id).headOption
    }
  }

  case class XX(code: Int, data: List[WebUser])

}