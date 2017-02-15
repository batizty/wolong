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
}

case class SparkJob(
  id: String,
  name: String,
  user_id: String,
  add_time: String,
  status: Int,
  user_class: String,
  user_jars: String,
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
  def driverCore(): Int = 1

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
      s"""/usr/local/spark/bin/spark-submit
          | --class ${user_class}
          | --name ${jobName}
          | --driver-memory ${driverMemory()}
          | --executor-memory ${executorMemory()}G
          | --total-executor-cores ${totalExecutorCores()}
          | ${user_jars} """.stripMargin
    arguments map { args =>
      cmd + args
    } getOrElse (cmd)
  }
}
//
//case class SparkJob(
//    task_id: String,
//    name: String,
//    core: String,
//    mem: String,
//    executor: String,
//    hdfs: String,
//    add_time: String,
//    status: String,
//    path: String,
//    user: String,
//    mesos_task_id: String = ""
//) extends Job {
//
//  import SparkJob._
//
//  implicit val format = Serialization.formats(NoTypeHints)
//
//  /* time format 2017-01-14 22:05:03 */
//  val datetime_fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
//
//  def Core = core.toLong
//
//  def Mem = core.toLong
//
//  def Executor = executor.toLong
//
//  def Hdfs = hdfs.toLong
//
//  def jobType: JobType.Value = JobType.SPARK
//
//  def jar: String = path
//
//  def toTask(): TaskDescriptor = {
//    TaskDescriptor(
//      getTaskName(),
//      getDriverResources(),
//      Left(CommandInfo(
//        shell = Some(true),
//        value = Some(toCmd)
//      ))
//    )
//  }
//
//  def jobId = task_id
//
//  def toCmd(): String = {
//    """/usr/local/spark/bin/spark-submit --class org.apache.spark.examples.SparkPi --executor-memory 1G --num-executors 2 /usr/local/spark/examples/jars/spark-examples_2.11-2.0.2.jar 1000"""
//  }
//
//  def jobName: String = name
//
//  def summary: String = {
//    s"name: $jobName status: $jobStatus user: $jobUserId submit_time: $jobAddTime"
//  }
//
//  def jobAddTime = DateTime.parse(add_time, datetime_fmt)
//
//  def jobStatus: JobStatus.Value = JobStatus.apply(status.toInt)
//
//  def jobUserId: String = user
//
//  def toJson(): String = Serialization.write(this)
//
//  def getTaskName(): String = {
//    List(jobId, jobName, toJson).mkString('\u0001'.toString)
//  }
//
//  def getDriverResources(): Seq[Resource] = {
//    Seq(
//      Resource(RESOURCE_CPU, Value.Type.SCALAR, Some(Value.Scalar(1.0))),
//      Resource(RESOURCE_MEM, Value.Type.SCALAR, Some(Value.Scalar(Mem)))
//    )
//  }
//
//  def getTotalResources(): Seq[Resource] = {
//    val driver = Seq(
//      Resource(RESOURCE_CPU, Value.Type.SCALAR, Some(Value.Scalar(1.0))),
//      Resource(RESOURCE_MEM, Value.Type.SCALAR, Some(Value.Scalar(Mem)))
//    )
//
//    val executor = Seq(
//      Resource(RESOURCE_CPU, Value.Type.SCALAR, Some(Value.Scalar(Core))),
//      Resource(RESOURCE_MEM, Value.Type.SCALAR, Some(Value.Scalar(Mem)))
//    )
//
//    var seq = driver
//    for { i <- 0L.to(Executor) } {
//      seq = seq ++ executor
//    }
//    seq
//  }
//
//}
