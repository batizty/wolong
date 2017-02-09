package com.weibo.datasys.job.mesos

import com.nokia.mesos.FrameworkFactory
import com.nokia.mesos.api.async.MesosDriver
import com.nokia.mesos.api.async.MesosFramework
import com.nokia.mesos.api.async.TaskLauncher

/**
  * Created by tuoyu on 09/02/2017.
  */

object WeiFrameworkFactory extends FrameworkFactory {
  override def createFramework(newDriver: () => MesosDriver): MesosFramework with TaskLauncher = {
    new WeiFrameworkImpl(newDriver)
  }
}
