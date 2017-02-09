package com.weibo.datasys.rest.data

import slick.jdbc.MySQLProfile.api._

/**
 * Created by tuoyu on 26/01/2017.
 */

case class DBGroup(
  id: Long,
  name: String,
  creator: String
) extends Group

/**
 * +---------------+--------------+------+-----+---------+----------------+
 * | Field         | Type         | Null | Key | Default | Extra          |
 * +---------------+--------------+------+-----+---------+----------------+
 * | id            | bigint(20)   | NO   | PRI | NULL    | auto_increment |
 * | catalogueName | varchar(255) | YES  |     | NULL    |                |
 * | createAt      | datetime     | YES  |     | NULL    |                |
 * | creator       | varchar(255) | YES  |     | NULL    |                |
 * | groupName     | varchar(255) | YES  |     | NULL    |                |
 * +---------------+--------------+------+-----+---------+----------------+
 */

class DBGroupTable(tag: Tag) extends Table[DBGroup](tag, "mm_group") {
  override def * = (id, name, creator) <> ((DBGroup.apply _).tupled, (DBGroup.unapply _))

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def name = column[String]("groupName")

  def creator = column[String]("creator")
}

