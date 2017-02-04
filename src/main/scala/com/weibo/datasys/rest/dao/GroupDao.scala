package com.weibo.datasys.rest.dao

import com.weibo.datasys.rest.Configuration
import com.weibo.datasys.rest.data.{DBGroup, DBGroupTable, Group}
import slick.jdbc.MySQLProfile.api._
import slick.lifted.TableQuery

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by tuoyu on 26/01/2017.
  */
trait GroupDao {
  def getAllGroup(): Future[List[Group]] = ???

  def getGroupById(id: Long): Future[Option[Group]] = ???

  def getGroupByName(name: String): Future[Option[Group]] = ???
}


class DBGroupDao
  extends GroupDao with Configuration {

  private val db = Database.forURL(
    url = db_url,
    user = db_user,
    password = db_passwd,
    driver = db_driver)

  val groups = TableQuery[DBGroupTable]

  override def getAllGroup(): Future[List[DBGroup]] = {
    db.run(groups.result).map(_.toList)
  }

  override def getGroupById(id: Long): Future[Option[DBGroup]] = {
    db.run(groups.filter(_.id === id).result.headOption)
  }

  override def getGroupByName(name: String): Future[Option[DBGroup]] = {
    db.run(groups.filter(_.name === name).result.headOption)
  }

}

class WebGroupDao
  extends GroupDao with Configuration {

}
