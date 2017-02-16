package com.weibo.datasys.rest.data

/**
 * Created by tuoyu on 25/01/2017.
 */
trait User {
  def id: String

  def name: String

  def groupId: String

  def isValid: Boolean
}

case class WebUser(
  user_id: String,
  name: String,
  auth: String,
  group_id: String
)
  extends User {
  val VALID_CODE = 0

  override def isValid: Boolean = {
    authFlag >= VALID_CODE
  }

  def authFlag: Int = auth.toInt

  def id: String = user_id

  def groupId: String = group_id
}