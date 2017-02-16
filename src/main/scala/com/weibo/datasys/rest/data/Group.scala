package com.weibo.datasys.rest.data

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
  groupName: String,
  core: String,
  mem: String,
  executor: String,
  hdfs: String,
  add_time: String
) extends Group {
  def groupId: String = group_id
  def groupCoreLimit: Long = core.toLong
  def groupMemLimit: Long = mem.toLong
  def groupHdfsLimit: Long = hdfs.toLong
}