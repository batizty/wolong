package com.weibo.datasys.rest.data

/**
  * Created by tuoyu on 25/01/2017.
  */
trait User {
  def id: Long

  def name: String

  def groupId: Long

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

  def id = user_id.toLong

  def groupId = group_id.toLong
}