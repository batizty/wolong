package com.weibo.datasys.rest.data

import scala.util.Try

/**
 * Created by tuoyu on 25/01/2017.
 */
trait Group {
  def groupId: String
  def groupName: String
  def groupCoreLimit: Long
  def groupMemLimit: Long
  def groupHdfsLimit: Long
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
  def groupId: String = group_id
  def groupName: String = name
  def groupCoreLimit: Long = Try { core.toLong } getOrElse(1000L)
  def groupMemLimit: Long = Try { mem.toLong } getOrElse(1000L)
  def groupHdfsLimit: Long = Try { hdfs.toLong } getOrElse(1000L)
}