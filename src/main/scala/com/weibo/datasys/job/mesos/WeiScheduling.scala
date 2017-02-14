package com.weibo.datasys.job.mesos

import com.nokia.mesos.api.async.Scheduling
import com.nokia.mesos.api.async.TaskLauncher._
import com.nokia.mesos.impl.launcher.WeiTaskAllocator
import org.apache.mesos.mesos.{Offer, OfferID}

import scala.collection.concurrent
import scala.concurrent.Future

/**
 * Created by tuoyu on 09/02/2017.
 */
class WeiScheduling extends Scheduling with WeiTaskAllocator {

  private[this] val currentOffers = new concurrent.TrieMap[OfferID, Offer]

  def offer(offers: Seq[Offer]): Unit = {
    currentOffers ++= offers.map(o => (o.id, o))
  }

  def schedule(tasks: Seq[TaskRequest], filter: Filter, urgency: Float): Future[TaskAllocation] = {
    val optAllocation: Option[TaskAllocation] = tryAllocate(
      currentOffers.values.toSeq,
      tasks,
      if (filter eq NoFilter) None else Some(filter)
    )
    optAllocation.fold {
      Future.failed[TaskAllocation](Scheduling.NoMatch())
    } { allocation =>
      // we cannot use these offers any more:
      this.rescind(allocation
        .filter { case (off, tsks) => tsks.nonEmpty }
        .map(_._1.id)
        .toSeq)

      Future.successful(allocation)
    }
  }

  def rescind(offers: Seq[OfferID]): Unit = {
    for (off <- offers) {
      currentOffers.remove(off)
    }
  }
}