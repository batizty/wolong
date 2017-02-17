package com.weibo.datasys.job

import com.weibo.datasys.job.data.{ JobStatus, Job }
import com.weibo.datasys.rest.Configuration
import com.weibo.datasys.rest.dao._
import com.weibo.datasys.rest.data.{ Group, Resource }

/**
 * Created by tuoyu on 09/02/2017.
 */
trait Scheduler {
  def getSatisfyJob(jobs: List[Job])(f: Option[Job] => Unit)(fLimitByCore: Job => Unit)(fLimitByMem: Job => Unit): Unit
}

trait SimpleSchedulerFIFO
    extends Scheduler
    with Configuration {

  import scala.concurrent.ExecutionContext.Implicits.global

  private val (userDao: UserDao, groupDao: GroupDao) =
    if (source == Configuration.DATA_SOURCE_DB) {
      (new DBUserDao(), new DBGroupDao())
    } else {
      (new WebUserDao(), new WebGroupDao())
    }

  //      val umap = users.map { u => (u.userId, u) } toMap
  def getSatisfyJob(
    jobs: List[Job]
  )(f: Option[Job] => Unit)(fLimitByCore: Job => Unit)(fLimitByMem: Job => Unit): Unit = {
    for {
      users <- userDao.getAllUser()
      groups <- groupDao.getAllGroup()
    } {
      val gmap = groups.map { g => (g.groupId, g) } toMap
      val umap = users.map { u => (u.name, u) } toMap

      val runingJobResources: Map[String, (Long, Long)] = jobs
        .filter(_.jobStatus == JobStatus.TaskRunning)
        .flatMap { job =>
          umap.get(job.jobUserId) map { u =>
            (u.userGroupId, (job.getTotalCores(), job.getTotalMemory()))
          }
        }.toMap

      val availableGroups: Map[String, (Long, Long)] = groups.map { group =>
        (group.groupId,
          runingJobResources.get(group.groupId).map {
            case ((cores, mems)) =>
              (group.groupCoreLimit - cores, group.groupMemLimit - mems)
          } getOrElse ((group.groupCoreLimit, group.groupMemLimit)))
      } toMap

      val jobOption = jobs.filter(_.canScheduler)
        .filter { job =>
          umap.get(job.jobUserId) match {
            case Some(u) =>
              availableGroups.get(u.userGroupId).exists {
                case ((core, mem)) =>
                  if (job.getTotalCores() > core) {
                    fLimitByCore(job)
                    false
                  } else if (job.getTotalMemory() > mem) {
                    fLimitByMem(job)
                    false
                  } else {
                    true
                  }
              }
            case None => false
          }
        }
        .sortBy(_.jobAddTime.getMillis)
        .headOption
      f(jobOption)
    }
  }
}
