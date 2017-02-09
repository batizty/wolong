package com.weibo.datasys.rest.data

/**
  * Created by tuoyu on 25/01/2017.
  */
trait Group {
  def id: Long
  def name: String
}

case class WebGroup(
                     group_id: String,
                     name: String,
                     core: String,
                     mem: String,
                     executor: String,
                     hdfs: String,
                     add_time: String
                   ) extends Group {
  def id = group_id.toLong
}