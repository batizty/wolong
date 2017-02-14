package com.weibo.datasys.rest

import com.typesafe.config.ConfigFactory

import scala.util.Try

/**
 * Created by tuoyu on 26/01/2017.
 */
trait Configuration {

  import Configuration._

  lazy val cluster_name = Try(config.getString("cluster.name")).getOrElse("wolong")

  // RestService配置
  lazy val host = Try(config.getString("service.host")).getOrElse("localhost")
  lazy val port = Try(config.getInt("service.port")).getOrElse(8080)
  lazy val expiredTime = Try(config.getInt("service.expiretime")).getOrElse(5)
  // 数据来源配置
  lazy val source = Try(config.getString("data.soruce")).getOrElse(DATA_SOURCE_DB)
  // DB配置
  lazy val db_host = Try(config.getString("db.host")).getOrElse("localhost")
  lazy val db_port = Try(config.getInt("db.port")).getOrElse(3306)
  lazy val db_name = Try(config.getString("db.name")).getOrElse("datasys_monitor")
  lazy val db_user = Try(config.getString("db.user")).getOrElse("hadoop")
  lazy val db_passwd = Try(config.getString("db.password")).getOrElse("hadoop")
  lazy val db_url =
    "jdbc:mysql://%s:%d/%s?autoReconnect=true&useUnicode=true&characterEncoding=utf-8".format(db_host, db_port, db_name)
  lazy val db_driver = "com.mysql.jdbc.Driver"
  // Web接口配置
  lazy val web_url_prefix = Try(config.getString("web.url_prefix")).getOrElse("http://mlplat.intra.weibo.com/math")
  lazy val web_user_url = web_url_prefix + "/" + Try(config.getString("web.user_url")).getOrElse("user")
  lazy val web_group_url = web_url_prefix + "/" + Try(config.getString("web.group_url")).getOrElse("getGroup")
  lazy val web_task_url = web_url_prefix + "/" + Try(config.getString("web.task_url")).getOrElse("getTask")
  lazy val web_update_task_url = web_url_prefix + "/" + Try(config.getString("web.update_task_url")).getOrElse("updateTask?task_id=%s&status=%s")
  lazy val web_timeout = Try(config.getInt("web.timeout")).getOrElse(5)
  // mesos 配置
  lazy val mesos_url = Try(config.getString("mesos.master")).getOrElse("10.77.136.42:5050")
  lazy val mesos_default_user = Try(config.getString("mesos.default_user")).getOrElse("hadoop")
  lazy val mesos_framework_name = Try(config.getString("mesos.framework.name")).getOrElse("weibo.wolong")
  // 读取RestService配置
  val config = ConfigFactory.load()
}

object Configuration extends {
  val DATA_SOURCE_DB = "db"
  val dATA_SOURCE_WEB = "web"
}
