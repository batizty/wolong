package com.weibo.datasys.job.mesos

import com.nokia.mesos.api.async.MesosException
import com.nokia.mesos.api.async.TaskLauncher.TaskDescriptor
import com.nokia.mesos.api.stream.MesosEvents.TaskEvent
import com.nokia.mesos.{DriverFactory, FrameworkFactory}
import com.weibo.datasys.rest.Configuration
import org.apache.mesos.mesos.{CommandInfo, FrameworkInfo, Resource, Value}
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}

/**
  * Created by tuoyu on 07/02/2017.
  */
object MesosSimpleHandler
  extends Configuration {
  val log = LoggerFactory.getLogger(getClass.getName)

  //  def main(args: Array[String]): Unit = {
  //    val fut = run(shellTaskDescriptor("sleep 10"))
  //    Await.ready(fut, 20.seconds)
  //  }

  def run(
           task: TaskDescriptor,
           user: Option[String] = None
         )(onTaskSubmitted: String => Unit)(onTaskFinished: => Unit)(onTaskError: => Unit): Future[Unit] = {
    val fw_info = FrameworkInfo(
      name = mesos_framework_name,
      user = mesos_default_user)

    val mk_dirver = DriverFactory.createDriver(fw_info, mesos_url)
    val fw = FrameworkFactory.createFramework(mk_dirver)
    val p = Promise[Unit]


    for {
      (fwId, master, driver) <- fw.connect()
      launched = fw.submitTask(task)
      task <- launched.info
    } {
      //TODO 这里后续改成
      // 1 一个actor对应一个任务
      // 2 te.state和JobStatus对应
      log.info(s"Task successfully started on slave ${task.slaveId.value}")
      onTaskSubmitted(task.taskId.toString())
      launched.events.subscribe(_ match {
        case te: TaskEvent if te.state.isTaskFinished =>
          onTaskFinished
          p.success(())
        case te: TaskEvent if (te.state.isTaskError ||
          te.state.isTaskFailed ||
          te.state.isTaskLost ||
          te.state.isTaskKilled ||
          te.state.isTaskKilling) =>
          onTaskError
          p.failure(new MesosException("task encountered error"))
        case _ =>
      })
    }

    for {
      _ <- p.future
      _ <- fw.terminate
    } yield ()
  }

  def shellTaskDescriptor(cmd: String): TaskDescriptor = {
    TaskDescriptor(
      "runSingleCommand task",
      Seq(Resource("cpus", Value.Type.SCALAR, Some(Value.Scalar(1.0)))),
      Left(CommandInfo(shell = Some(true), value = Some(cmd)))
    )
  }

}
