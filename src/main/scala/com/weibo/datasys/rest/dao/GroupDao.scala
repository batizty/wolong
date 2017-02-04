package com.weibo.datasys.rest.dao

import com.weibo.datasys.rest.Configuration
import com.weibo.datasys.rest.data._
import org.json4s.DefaultFormats
import org.json4s.native.JsonMethods._
import org.slf4j.LoggerFactory
import slick.jdbc.MySQLProfile.api._
import slick.lifted.TableQuery

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by tuoyu on 26/01/2017.
  */
trait GroupDao {
  def getAllGroup(): Future[List[Group]]

  def getGroupById(id: Long): Future[Option[Group]]

  def getGroupByName(name: String): Future[Option[Group]]
}


class DBGroupDao
  extends GroupDao with Configuration {

  val groups = TableQuery[DBGroupTable]
  private val db = Database.forURL(
    url = db_url,
    user = db_user,
    password = db_passwd,
    driver = db_driver)

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
  extends GroupDao
    with Configuration {
  val log = LoggerFactory.getLogger(getClass.getName)
  implicit val formats = DefaultFormats

  override def getGroupById(id: Long): Future[Option[Group]] = {
    getAllGroup() map { gs =>
      gs.filter(_.id == id).headOption
    }
  }

  override def getAllGroup(): Future[List[Group]] = {
    import com.weibo.datasys.rest.util.WebClient
    WebClient.accessURL[String](web_group_url) map { ssOption =>
      ssOption map { ss =>
        try {
          parse(ss).extract[XX].data
        } catch {
          case err: Throwable =>
            log.error(s"Extract WebGroupDao failed with Message : ${err.getMessage}")
            List.empty
        }
      } getOrElse List.empty
    }
  }

  override def getGroupByName(name: String): Future[Option[Group]] = {
    getAllGroup() map { gs =>
      gs.filter(_.name == name).headOption
    }
  }

  case class XX(code: Int, data: List[WebGroup])
}
