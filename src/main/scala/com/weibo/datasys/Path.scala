package com.weibo.datasys

/**
 * Created by tuoyu on 03/02/2017.
 */
object Path extends Enumeration {
  val AUTH = "Auth"
  val CLUSTER = "Cluster"
  val SCHEDULER = "Scheduler"
  val xx = "xx"
}

object SecondPath extends Enumeration {
  val HADOOP_POLICY_XML = "hadoop-policy.xml"
  val ADD_USER_SHELL = "add_user_to_hadoop.sh"
  val AUTHORIZED_CHECK = "authorized_check"
  val SPARK_JOB = "spark_job"
}
