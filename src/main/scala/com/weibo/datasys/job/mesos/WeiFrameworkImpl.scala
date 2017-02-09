package com.weibo.datasys.job.mesos

import com.nokia.mesos.api.async.{MesosDriver, Scheduling}
import com.nokia.mesos.impl.launcher.AbstractFrameworkImpl

/**
 * Created by tuoyu on 09/02/2017.
 */
class WeiFrameworkImpl(mkDriver: () => MesosDriver)
  extends AbstractFrameworkImpl(mkDriver) {

  override val scheduling: Scheduling =
    new WeiScheduling
}