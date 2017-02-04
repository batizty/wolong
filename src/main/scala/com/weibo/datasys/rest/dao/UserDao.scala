package com.weibo.datasys.rest.dao

import com.weibo.datasys.rest.Configuration
import com.weibo.datasys.rest.data.{DBUserTable, User}
import org.slf4j.LoggerFactory
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
/**
  * Created by tuoyu on 26/01/2017.
  */
trait UserDao {
  def getAllUser(): Future[List[User]] = ???

  def getUserByName(name: String): Future[Option[User]] = ???

  def getUserById(id: Long): Future[Option[User]] = ???
}

class DBUserDao
  extends UserDao
    with Configuration {
  val log = LoggerFactory.getLogger("DBUserDao")

  private val db = Database.forURL(
    url = db_url,
    user = db_user,
    password = db_passwd,
    driver = db_driver
  )

  val users = TableQuery[DBUserTable]

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
  extends UserDao with Configuration {
  // TODO override all data

}