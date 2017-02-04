package com.weibo.datasys.rest.data

import java.sql.Date

import org.joda.time.DateTime
import slick.jdbc.MySQLProfile.api._

/**
  * Created by tuoyu on 26/01/2017.
  */

case class DBUser(
                   id: Long,
                   groupId: Long,
                   state: String,
                   name: String
                 ) extends User {
  override def isValid: Boolean = {
    // TODO
    true
  }
}

/**
  * +---------------+--------------+------+-----+---------+----------------+
  * | Field         | Type         | Null | Key | Default | Extra          |
  * +---------------+--------------+------+-----+---------+----------------+
  * | id            | bigint(20)   | NO   | PRI | NULL    | auto_increment |
  * | createAt      | datetime     | YES  |     | NULL    |                |
  * | groupId       | bigint(20)   | NO   |     | NULL    |                |
  * | indentfy      | int(11)      | NO   |     | NULL    |                |
  * | lastLoginTime | datetime     | YES  |     | NULL    |                |
  * | password      | varchar(255) | YES  |     | NULL    |                |
  * | state         | varchar(255) | YES  |     | NULL    |                |
  * | userName      | varchar(255) | YES  | UNI | NULL    |                |
  * +---------------+--------------+------+-----+---------+----------------+
  */

class DBUserTable(tag: Tag) extends Table[DBUser](tag, "mm_user") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def state = column[String]("state")

  def name = column[String]("userName")

  def groupId = column[Long]("groupId")

  override def * = (id, groupId, state, name) <>((DBUser.apply _).tupled, DBUser.unapply)
}
