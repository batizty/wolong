package com.nokia.mesos.impl.launcher

import com.weibo.datasys.job.data.SparkJob
import org.apache.mesos.mesos.Resource

import scala.annotation.tailrec

import org.apache.mesos.mesos

import com.nokia.mesos.api.async.TaskLauncher
import com.typesafe.scalalogging.LazyLogging

import scala.util.Try

trait WeiTaskAllocator extends LazyLogging {

  import TaskLauncher._

  def tryAllocate(offers: Seq[mesos.Offer], tasks: Seq[TaskRequest], optFilter: Option[Filter]): Option[TaskAllocation] = {
    logger info s"Searching for valid allocation for tasks ${tasks} in offers: $offers"
    val taskList = tasks.map(_.desc).toList

    // TODO 这里加上资源处理
    // 从TaskDescriptor里面解释出来Json的数据格式，然后进行处理,计算资源总的消耗量

    // optimization: first quickly check approximate resource constraints
    if (trySimpleAllocation(ResourceProcessor.sumResources(offers.flatMap(_.resources)), taskList)) {
      // generate actual allocations, and verify them:
      val filter: (TaskAllocation => Boolean) = optFilter match {
        case Some(f) => (m => resourceFilter(m) && f(m))
        case None => resourceFilter
      }
      val res = generateAllocations(offers, tasks.toList).find(filter)
      res.fold(logger info "No valid allocation found")(all => logger info s"Found valid allocation: $all")
      res
    } else {
      // it's impossible to create a correct allocation,
      // so we don't even have to try generating them:
      logger info s"No allocation possible: offer have insufficient resources to launch all tasks"
      None
    }
  }

  def resourceFilter: Filter = (m: TaskAllocation) => {
    m.forall {
      case (offer, tasks) => {
        val remains = tasks.foldLeft(Option(offer.resources.toVector)) {
          case (Some(remainder), task) =>
            ResourceProcessor.remainderOf(remainder, task.desc.resources)
          case (None, _) =>
            None
        }

        remains.isDefined
      }
    }
  }

  /** Simply checks whether all the resources are enough to launch all the tasks */
  @tailrec
  private[mesos] final def trySimpleAllocation(rs: Vector[mesos.Resource], tasks: Seq[TaskDescriptor]): Boolean = tasks.to[List] match {
    case Nil =>
      true
    case task :: rest =>
      import org.json4s.DefaultFormats
      import org.json4s.native.JsonMethods._
      implicit val formats = DefaultFormats

      val resources = Try {
        val Array(jid, jname, jjson) = task.name.split('\u0001')
        val job = parse(jjson).extract[SparkJob]
        job.getTotalResources()
      } getOrElse (task.resources)

      ResourceProcessor.remainderOf(rs, resources) match {
        case Some(remainder) => trySimpleAllocation(remainder, rest)
        case None => false
      }
  }

  private[mesos] def generateAllocations(offers: Seq[mesos.Offer], tasks: List[TaskRequest]): Iterator[TaskAllocation] = {
    val a = TaskAllocator.allocate(offers, tasks)
    a.map(_.mapValues(_.to[List])).iterator
  }
}

private[mesos] object WeiTaskAllocator {

  private[mesos] def allocate[O, T](offers: TraversableOnce[O], tasks: TraversableOnce[T]): Stream[Map[O, Stream[T]]] = {
    allocateImpl(offers, tasks) map (_.groupBy(_._1).mapValues(_.map(_._2)))
  }

  private[mesos] def allocateImpl[O, T](offers: TraversableOnce[O], tasks: TraversableOnce[T]): Stream[Stream[(O, T)]] = {
    val os = offers.toStream
    val ts = tasks.toStream
    val r = for {
      t <- ts
    } yield {
      for {
        o <- os
      } yield (o, t)
    }

    sequenceStream(r)
  }

  /**
   * Lazy Cartesian product of a `Stream` of `Stream`s
   */
  private[mesos] def sequenceStream[A](ss: Stream[Stream[A]]): Stream[Stream[A]] = {
    val z = Stream.empty[A] #:: Stream.empty
    ss.foldRight(z) { (s, acc) =>
      s.flatMap { a =>
        acc.map(a #:: _)
      }
    }
  }
}
