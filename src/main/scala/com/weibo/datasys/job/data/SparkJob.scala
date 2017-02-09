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
      case "0" => JobStatus.TaskStaging
      case "1" => JobStatus.TaskFinished
      case _ => JobStatus.TaskNotSupport
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
                     user: String,
                     mesos_task_id: String = ""
                   ) extends Job {

  /* time format 2017-01-14 22:05:03 */
  val datetime_fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")

  def Core = core.toLong

  def Mem = core.toLong

  def Executor = executor.toLong

  def Hdfs = hdfs.toLong

  def jobType: JobType.Value = JobType.SPARK

  def jar: String = path

  def toTask(): TaskDescriptor = {
    // TODO  这里会有详细的配置生成
    import org.apache.mesos.mesos.{CommandInfo, Resource, Value}
    TaskDescriptor(
      jobId + "_" + jobName,
      Seq(Resource("cpus", Value.Type.SCALAR, Some(Value.Scalar(1.0)))),
      Left(CommandInfo(
        shell = Some(true),
        value = Some(toCmd)
      ))
    )
  }

  def jobId = task_id

  def toCmd(): String = {
    """/usr/local/spark/bin/spark-submit --class org.apache.spark.examples.SparkPi --executor-memory 10G --num-executors 4 /usr/local/spark/examples/jars/spark-examples_2.11-2.0.2.jar 10000"""
  }

  def jobName: String = name

  def summary: String = {
    s"name: $jobName status: $jobStatus user: $jobUser submit_time: $jobSubmitTime"
  }

  def jobSubmitTime = DateTime.parse(add_time, datetime_fmt)

  def jobStatus: JobStatus.Value = JobStatus.apply(status.toInt)

  def jobUser: String = user

}
