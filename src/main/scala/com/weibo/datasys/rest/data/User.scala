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
                  id: Long,
                  name: String,
                  groupId: Long
                  )
  extends User {
  override def isValid: Boolean = {
    true
  }
}