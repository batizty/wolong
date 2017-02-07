package com.weibo.datasys.job.data

import com.nokia.mesos.api.async.TaskLauncher.TaskDescriptor
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

/**
  * Created by tuoyu on 06/02/2017.
  */

object SparkJob {
  implicit def statusToJobStatus(s: String): JobStatus.Value = {
    s match {
      case "0" => JobStatus.SUSPENDING
      case "1" => JobStatus.FINISHED
      case _ => JobStatus.NOT_SUPPORT
    }
  }

  implicit def JobStatusToStatus(jobStatus: JobStatus.Value): String = {
    jobStatus.id.toString
  }
}

case class SparkJob(
                     task_id: String,
                     name: String,
                     core: String,
                     mem: String,
                     executor: String,
                     hdfs: String,
                     add_time: String,
                     status: String,
                     path: String,
                     user: String) extends Job {

  /* time format 2017-01-14 22:05:03 */
  val datetime_fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")

  def Core = core.toLong

  def Mem = core.toLong

  def Executor = executor.toLong

  def Hdfs = hdfs.toLong

  def jobSubmitTime = DateTime.parse(add_time, datetime_fmt)

  def jobStatus: JobStatus.Value = status match {
    case "0" => JobStatus.SUSPENDING
    case _ => JobStatus.NOT_SUPPORT
  }

  def jobType: JobType.Value = JobType.SPARK

  def jobUser: String = user

  def jar: String = path

  def toTask(): TaskDescriptor = {
    import org.apache.mesos.mesos.{CommandInfo, Resource, Value}
    TaskDescriptor(
      jobId + "_" + jobName,
      Seq(Resource("cpus", Value.Type.SCALAR, Some(Value.Scalar(1.0)))),
      Left(CommandInfo(
        shell = Some(true),
        value = Some(toCmd))
      )
    )
  }

  def jobId = task_id

  def jobName: String = name

  def toCmd(): String = {
    """spark-submit --class org.apache.spark.examples.SparkPi --executor-memory 1G --num-executors  2 /usr/local/spark/examples/jars/spark-examples_2.11-2.0.2.jar 100"""
  }

}
