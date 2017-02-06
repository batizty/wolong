package com.weibo.datasys.job.data

import org.joda.time.DateTime

/**
  * Created by tuoyu on 06/02/2017.
  */
trait Job {
  def jobType: JobType.Value

  def jobName: String

  def jobUser: String

  def jobSubmitTime: DateTime

  def jobStatus: JobStatus.Value

  def jobId: String
}

object JobType extends Enumeration {
  val HADOOP = Value
  val SPARK = Value
}

object JobStatus extends Enumeration {
  val SUSPENDING = Value
  val RUNNING = Value
  val FINISHED = Value
  val KILLED = Value
  val FAILED = Value
  val NOT_SUPPORT = Value
}
