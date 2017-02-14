package com.weibo.datasys.rest.data

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
  val ACTIVED_FLAG = "actived"
  override def isValid: Boolean = state == ACTIVED_FLAG
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
  override def * = (id, groupId, state, name) <> ((DBUser.apply _).tupled, DBUser.unapply)

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def state = column[String]("state")

  def name = column[String]("userName")

  def groupId = column[Long]("groupId")
}
