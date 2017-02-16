package com.weibo.datasys.job.data

import com.nokia.mesos.api.async.TaskLauncher.TaskDescriptor
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.json4s.NoTypeHints
import org.json4s.native.Serialization
import org.apache.mesos.mesos.{CommandInfo, Resource, Value}

/**
 * Created by tuoyu on 06/02/2017.
 */

object SparkJob {
  val RESOURCE_CPU = "cpus"
  val RESOURCE_MEM = "mem"
  val RESOURCE_DISK = "disks"
}

case class SparkJob(
  id: String,
  name: String,
  user_id: String,
  add_time: String,
  status: Int,
  user_class: String,
  user_jars: String,
  driver_cores: Int = 1,
  driver_memory: Option[Int] = None,
  executor_memory: Option[Int] = None,
  total_executor_cores: Option[Int] = None,
  confs: String,
  arguments: Option[String] = None,
  mesos_task_id: Option[String] = None,
  mesos_memory_usage: Option[Int] = None,
  mesos_core_usage: Option[Int] = None
) extends Job {

  import SparkJob._

  /* default values for this Spark Jobs */
  implicit val format = Serialization.formats(NoTypeHints)

  /* time format 2017-01-14 22:05:03 */
  val datetime_fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")

  // TODO 这个需要后续做成准确的，然后回给结果
  val default_max_core_in_executor: Int = 8

  /* Base Job Properties */
  def jobType: JobType.Value = JobType.SPARK

  def jobId: String = id

  def jobName: String = name

  def jobUserId: String = user_id

  def jobAddTime: DateTime = DateTime.parse(add_time, datetime_fmt)

  def jobStatus: JobStatus.Value = JobStatus.apply(status.toInt)

  def summary: String = {
    "job id : " + jobId + " -> " + toJson()
  }

  /* Spark Job Properties */
  def driverCore(): Int = driver_cores

  def driverMemory(): Int = driver_memory.getOrElse(1)

  def executorMemory(): Int = executor_memory.getOrElse(1)

  def totalExecutorCores(): Int = total_executor_cores.getOrElse(2)

  def toJson(): String = Serialization.write(this)

  /* For Mesos Job Sumbit */
  def getDriverResource(): Seq[Resource] = {
    Seq(
      Resource(RESOURCE_CPU, Value.Type.SCALAR, Some(Value.Scalar(driverCore()))),
      Resource(RESOURCE_MEM, Value.Type.SCALAR, Some(Value.Scalar(driverMemory())))
    )
  }

  def getTotalCores(): Int = driverCore() + totalExecutorCores()

  def getTotalMemory(): Int = driverMemory() + getExecutorMemory()

  def getExecutors(): Int = totalExecutorCores() / default_max_core_in_executor

  def getExecutorMemory(): Int = executorMemory() * getExecutors()

  def getExecutorResource(): Seq[Resource] = {
    Seq(
      Resource(RESOURCE_CPU, Value.Type.SCALAR, Some(Value.Scalar(totalExecutorCores()))),
      Resource(RESOURCE_MEM, Value.Type.SCALAR, Some(Value.Scalar(math.ceil(getExecutorMemory()))))
    )
  }

  def getTotalResources(): Seq[Resource] = {
    getDriverResource() ++ getExecutorResource()
  }

  def getTaskName(): String = {
    List(jobId, jobName, toJson).mkString('\u0001'.toString)
  }

  def getTask(): TaskDescriptor = TaskDescriptor(
    getTaskName(),
    getDriverResource(),
    Left(CommandInfo(shell = Some(true), value = Some(getShellCommand)))
  )

  def getShellCommand(): String = {
    val cmd =
      "/usr/local/spark/bin/spark-submit " +
        "--class " + user_class + " " +
        "--name " + jobName + " " +
        "--driver-memory " + driverMemory() + "G " +
        "--executor-memory " + executorMemory() + "G " +
        "--total-executor-cores " + totalExecutorCores() + " " + user_jars
    arguments map { args =>
      cmd + " " + args
    } getOrElse (cmd)
  }
}
