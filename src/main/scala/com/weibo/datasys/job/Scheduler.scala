package com.weibo.datasys.job

import com.weibo.datasys.job.data.Job
import com.weibo.datasys.rest.data.Resource

/**
 * Created by tuoyu on 09/02/2017.
 */
trait Scheduler {
  def getSatisfyJob(jobs: List[Job], resources: List[Resource]): Option[Job]
}

trait SimpleSchedulerFIFO extends Scheduler {
  def getSatisfyJob(
    jobs: List[Job],
    resources: List[Resource] = List.empty
  ): Option[Job] = {
    jobs.filter(_.canScheduler)
      .sortBy(_.jobAddTime.getMillis)
      .headOption

    // TODO
    // 1 组权限检查
    // 2 组资源检查(cpu, mem, disk)

  }
}
