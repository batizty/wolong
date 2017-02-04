package com.weibo.datasys.rest.data

/**
  * Created by tuoyu on 25/01/2017.
  */
trait Resource {
  def disk: Long
  def cpu: Double
  def mem: Long
}
