package com.weibo.datasys.rest.data

/**
  * Created by tuoyu on 25/01/2017.
  */
trait Group {
  def id: Long
  def name: String
}


case class WebGroup(
                   id: Long,
                   name: String
                   )
  extends Group