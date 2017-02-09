package com.weibo.datasys.job.data

import org.apache.mesos.mesos.TaskState
import org.apache.mesos.mesos.TaskState._
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

  def summary: String

  /**
   * TODO 这里缺少一个状态，降级，针对这个状态，需要有一个策略
   *
   * @return
   */
  def canScheduler: Boolean = {
    jobStatus match {
      case JobStatus.TaskStaging |
        JobStatus.TaskStarting |
        JobStatus.TaskRunning => true
      case JobStatus.TaskKilling |
        JobStatus.TaskFinished |
        JobStatus.TaskFailed |
        JobStatus.TaskKilled |
        JobStatus.TaskLost |
        JobStatus.TaskError => false
    }
  }

  def isFinishedOrFailure: Boolean = {
    jobStatus match {
      case JobStatus.TaskFinished |
        JobStatus.TaskFailed |
        JobStatus.TaskKilled |
        JobStatus.TaskLost |
        JobStatus.TaskError => true
      case _ => false
    }
  }
}

object JobType extends Enumeration {
  val HADOOP = Value
  val SPARK = Value
}

object JobStatus extends Enumeration {
  val TaskStaging = Value
  val TaskStarting = Value
  val TaskRunning = Value
  val TaskKilling = Value
  val TaskFinished = Value
  val TaskFailed = Value
  val TaskKilled = Value
  val TaskLost = Value
  val TaskError = Value

  val TaskNotSupport = Value

  implicit def apply1(stat: TaskState): JobStatus.Value = {
    stat match {
      case TASK_STAGING => TaskStaging
      case TASK_STARTING => TaskStarting
      case TASK_RUNNING => TaskRunning
      case TASK_KILLING => TaskKilling
      case TASK_FINISHED => TaskFinished
      case TASK_FAILED => TaskFailed
      case TASK_KILLED => TaskKilled
      case TASK_LOST => TaskLost
      case TASK_ERROR => TaskError
    }
  }
}
