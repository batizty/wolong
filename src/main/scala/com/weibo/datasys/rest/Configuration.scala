package com.weibo.datasys.rest

import com.typesafe.config.ConfigFactory

import scala.util.Try

/**
  * Created by tuoyu on 26/01/2017.
  */
trait Configuration {

  import Configuration._

  // 读取RestService配置
  val config = ConfigFactory.load()

  // RestService配置
  lazy val host = Try(config.getString("service.host")).getOrElse("localhost")
  lazy val port = Try(config.getInt("service.port")).getOrElse(8080)
  lazy val expiredTime = Try(config.getInt("service.expiretime")).getOrElse(5)

  // 数据来源配置
  lazy val source = Try(config.getString("data.soruce")).getOrElse(DATA_SOURCE_DB)

  // DB配置
  lazy val db_host = Try(config.getString("db.host")).getOrElse("localhost")
  lazy val db_port = Try(config.getInt("db.port")).getOrElse(3306)
  lazy val db_name = Try(config.getString("db.name")).getOrElse("")
  // TODO
  lazy val db_user = Try(config.getString("db.user")).getOrElse("hadoop")
  lazy val db_passwd = Try(config.getString("db.password")).getOrElse("hadoop")

  lazy val db_url =
    "jdbc:mysql://10.77.136.64:3306/datasys_monitor?autoReconnect=true&useUnicode=true&characterEncoding=utf-8"

  lazy val db_driver = "com.mysql.jdbc.Driver"
  // Web接口配置
  lazy val web_url = Try(config.getString("url")).getOrElse("http://") // TODO
}

object Configuration extends {
  val DATA_SOURCE_DB = "db"
  val dATA_SOURCE_WEB = "web"
}
