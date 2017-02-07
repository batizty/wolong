package com.weibo.datasys.job.mesos

import com.nokia.mesos.api.async.MesosException
import com.nokia.mesos.api.async.TaskLauncher.TaskDescriptor
import com.nokia.mesos.api.stream.MesosEvents.TaskEvent
import com.nokia.mesos.{DriverFactory, FrameworkFactory}
import com.weibo.datasys.rest.Configuration
import org.apache.mesos.mesos.{CommandInfo, FrameworkInfo, Resource, Value}
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future, Promise}

/**
  * Created by tuoyu on 07/02/2017.
  */
object MesosSimpleHandler
  extends Configuration {
  val log = LoggerFactory.getLogger(getClass.getName)

  def main(args: Array[String]): Unit = {
    val fut = run(shellTaskDescriptor("sleep 10"))
    Await.ready(fut, 20.seconds)
  }

  def run(task: TaskDescriptor): Future[Unit] = {
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
      log.info(s"Task successfully started on slave ${task.slaveId.value}")
      launched.events.subscribe(_ match {
        case te: TaskEvent if te.state.isTaskFinished => p.success(())
        case te: TaskEvent if (te.state.isTaskError || te.state.isTaskFailed ||
          te.state.isTaskLost || te.state.isTaskKilled || te.state.isTaskKilling) =>
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
