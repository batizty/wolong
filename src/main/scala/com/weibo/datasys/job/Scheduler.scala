package com.weibo.datasys.job

import akka.actor.ActorRef
import com.weibo.datasys.job.data.{JobStatus, Job}
import com.weibo.datasys.rest.Configuration
import com.weibo.datasys.rest.dao._
import com.weibo.datasys.rest.data.Resource

/**
 * Created by tuoyu on 09/02/2017.
 */
trait Scheduler {
  def getSatisfyJob(jobs: List[Job], resources: List[Resource]): Option[Job]
}

trait SimpleSchedulerFIFO
  extends Scheduler
  with Configuration {
  //self: ActorRef =>

  private val (userDao: UserDao, groupDao: GroupDao) =
    if (source == Configuration.DATA_SOURCE_DB) {
      (new DBUserDao(), new DBGroupDao())
    } else {
      (new WebUserDao(), new WebGroupDao())
    }

  //  TODO 加上限制条件
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

  //  def freshJobStatus(jobs: List[Job]): Unit = {
  //    val runnigJobs = jobs.filter { job =>
  //     job.jobStatus == JobStatus.TaskRunning
  //    }
  //
  //
  //    userDao.getAllUser()
  //    groupDao.getAllGroup()
  //
  //    val resourcesMap = runnigJobs map { job =>
  //      (job.jobUserId, (job.getTotalCores, job.getTotalMemory))
  //    } toMap
  //      .reduce
  //
  //
  //
  //    ()
  //  }
}
