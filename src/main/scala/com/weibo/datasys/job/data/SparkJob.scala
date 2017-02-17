package com.weibo.datasys.job.data

import com.nokia.mesos.api.async.TaskLauncher.TaskDescriptor
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.json4s.NoTypeHints
import org.json4s.native.Serialization
import org.apache.mesos.mesos.{ CommandInfo, Resource, Value }

/**
 * Created by tuoyu on 06/02/2017.
 */

object SparkJob {
  val RESOURCE_CPU = "cpus"
  val RESOURCE_MEM = "mem"
  val RESOURCE_DISK = "disks"
}

/*
    "task_id": "16",
    "name": "123",
    "core": "1",
    "mem": "1",
    "executor": "11",
    "hdfs": "1",
    "add_time": "2017-02-16 16:57:19",
    "status": "TaskStaging",
    "path": "http://mlplat.intra.weibo.com/data/2017/0216/tingting43/1487235438-spark-examples_2.11-2.0.2.jar",
    "user": "tingting43"
 */
case class SparkJob(
    task_id: String,
    name: String,
    user: String,
    add_time: String,
    status: String,
    user_class: String = "org.apache.spark.examples.SparkPi",
    user_jars: String = "/tmp/spark-examples_2.11-2.0.2.jar",
    driver_cores: Long = 1L,
    driver_memory: Option[Long] = None,
    executor_memory: Option[Long] = None,
    total_executor_cores: Option[Long] = None,
    confs: String = "",
    arguments: Option[String] = Some("1000"),
    mesos_task_id: Option[String] = None,
    mesos_memory_usage: Option[Long] = None,
    mesos_core_usage: Option[Long] = None,
    core: Int = 0,
    mem: Int = 0,
    executor: Int = 0,
    hdfs: Int = 0,
    path: String
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

  def jobId: String = task_id

  def jobName: String = name

  def jobUserId: String = user

  def jobAddTime: DateTime = DateTime.parse(add_time, datetime_fmt)

  def jobStatus: JobStatus.Value = JobStatus.apply(status.toInt)

  def summary: String = {
    "job id : " + jobId + " -> " + s"name: $jobName status: $jobStatus"
  }

  /* Spark Job Properties */
  def driverCore(): Long = driver_cores

  def driverMemory(): Long = driver_memory.getOrElse(1)

  def executorMemory(): Long = executor_memory.getOrElse(1)

  def totalExecutorCores(): Long = total_executor_cores.getOrElse(4)

  def toJson(): String = Serialization.write(this)

  /* For Mesos Job Sumbit */
  def getDriverResource(): Seq[Resource] = {
    Seq(
      Resource(RESOURCE_CPU, Value.Type.SCALAR, Some(Value.Scalar(driverCore()))),
      Resource(RESOURCE_MEM, Value.Type.SCALAR, Some(Value.Scalar(driverMemory())))
    )
  }

  def getTotalCores(): Long = driverCore() + totalExecutorCores()

  def getTotalMemory(): Long = driverMemory() + getExecutorMemory()

  def getExecutors(): Long = totalExecutorCores() / default_max_core_in_executor

  def getExecutorMemory(): Long = executorMemory() * getExecutors()

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
    val cmd = Seq(
      "/usr/local/spark/bin/spark-submit",
      "--class", user_class,
      "--name", jobName,
      "--driver-cores", driverCore(),
      "--driver-memory", driverMemory() + "G ",
      "--executor-memory", executorMemory() + "G ",
      "--total-executor-cores", totalExecutorCores(),
      user_jars
    ).mkString(" ")

    arguments map { args =>
      Seq(cmd, args).mkString(" ")
    } getOrElse (cmd)
  }
}
//
//case class SparkJob(
//  id: String,
//  name: String,
//  user_id: String,
//  add_time: String,
//  status: Int = 0,
//  user_class: String,
//  user_jars: String,
//  driver_cores: Long = 1L,
//  driver_memory: Option[Long] = None,
//  executor_memory: Option[Long] = None,
//  total_executor_cores: Option[Long] = None,
//  confs: String,
//  arguments: Option[String] = None,
//  mesos_task_id: Option[String] = None,
//  mesos_memory_usage: Option[Long] = None,
//  mesos_core_usage: Option[Long] = None
//) extends Job {
//
//  import SparkJob._
//
//  /* default values for this Spark Jobs */
//  implicit val format = Serialization.formats(NoTypeHints)
//
//  /* time format 2017-01-14 22:05:03 */
//  val datetime_fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
//
//  // TODO 这个需要后续做成准确的，然后回给结果
//  val default_max_core_in_executor: Int = 8
//
//  /* Base Job Properties */
//  def jobType: JobType.Value = JobType.SPARK
//
//  def jobId: String = id
//
//  def jobName: String = name
//
//  def jobUserId: String = user_id
//
//  def jobAddTime: DateTime = DateTime.parse(add_time, datetime_fmt)
//
//  def jobStatus: JobStatus.Value = JobStatus.apply(status.toInt)
//
//  def summary: String = {
//    "job id : " + jobId + " -> " + s"name: $jobName status: $jobStatus"
//  }
//
//  /* Spark Job Properties */
//  def driverCore(): Long = driver_cores
//
//  def driverMemory(): Long = driver_memory.getOrElse(1)
//
//  def executorMemory(): Long = executor_memory.getOrElse(1)
//
//  def totalExecutorCores(): Long = total_executor_cores.getOrElse(2)
//
//  def toJson(): String = Serialization.write(this)
//
//  /* For Mesos Job Sumbit */
//  def getDriverResource(): Seq[Resource] = {
//    Seq(
//      Resource(RESOURCE_CPU, Value.Type.SCALAR, Some(Value.Scalar(driverCore()))),
//      Resource(RESOURCE_MEM, Value.Type.SCALAR, Some(Value.Scalar(driverMemory())))
//    )
//  }
//
//  def getTotalCores(): Long = driverCore() + totalExecutorCores()
//
//  def getTotalMemory(): Long = driverMemory() + getExecutorMemory()
//
//  def getExecutors(): Long = totalExecutorCores() / default_max_core_in_executor
//
//  def getExecutorMemory(): Long = executorMemory() * getExecutors()
//
//  def getExecutorResource(): Seq[Resource] = {
//    Seq(
//      Resource(RESOURCE_CPU, Value.Type.SCALAR, Some(Value.Scalar(totalExecutorCores()))),
//      Resource(RESOURCE_MEM, Value.Type.SCALAR, Some(Value.Scalar(math.ceil(getExecutorMemory()))))
//    )
//  }
//
//  def getTotalResources(): Seq[Resource] = {
//    getDriverResource() ++ getExecutorResource()
//  }
//
//  def getTaskName(): String = {
//    List(jobId, jobName, toJson).mkString('\u0001'.toString)
//  }
//
//  def getTask(): TaskDescriptor = TaskDescriptor(
//    getTaskName(),
//    getDriverResource(),
//    Left(CommandInfo(shell = Some(true), value = Some(getShellCommand)))
//  )
//
//  def getShellCommand(): String = {
//    val cmd = Seq(
//      "/usr/local/spark/bin/spark-submit",
//      "--class", user_class,
//      "--name", jobName,
//      "--driver-cores", driverCore(),
//      "--driver-memory", driverMemory() + "G ",
//      "--executor-memory", executorMemory() + "G ",
//      "--total-executor-cores", totalExecutorCores(),
//      user_jars
//    ).mkString(" ")
//
//    arguments map { args =>
//      Seq(cmd, args).mkString(" ")
//    } getOrElse (cmd)
//  }
//}
