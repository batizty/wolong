package com.weibo.datasys.job.mesos

import com.nokia.mesos.FrameworkFactory
import com.nokia.mesos.api.async.{MesosDriver, MesosFramework, TaskLauncher}

/**
  * Created by tuoyu on 09/02/2017.
  */

object WeiFrameworkFactory extends FrameworkFactory {
  override def createFramework(newDriver: () => MesosDriver): MesosFramework with TaskLauncher = {
    new WeiFrameworkImpl(newDriver)
  }
}
