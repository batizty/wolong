package com.weibo.datasys.rest.util

import java.io.InputStream

import com.weibo.datasys.rest.data.{DBGroup, DBUser, User, Group}
import org.slf4j.LoggerFactory

import scala.io.Source

/**
  * Created by tuoyu on 04/02/2017.
  */
object HadoopShellSettor {
  val log = LoggerFactory.getLogger(getClass.getName)

  val default_xml = "add_user_to_hadoop.sh"

  def getValidHadoopShell(
                           users: List[User],
                           groups: List[Group],
                           path: Option[String] = None): Option[String] = {
    val name = path.getOrElse(default_xml)
    try {
      val stream: InputStream = getClass.getResourceAsStream("/" + name)
      val lines = Source.fromInputStream(stream).mkString
      val gmap = groups.map(g => (g.id, g.name)).toMap
      val ug = users.map { u =>
        gmap.get(u.groupId).map(gn => (u.name, gn))
      }.flatten

      if (ug.nonEmpty) {
        val uarray = "user_array=(" + ug.map(_._1).mkString(" ") + ")"
        val garray = "group_array=(" + ug.map(_._2).mkString(" ") + ")"
        val line1 = lines.replace("user_array=()", uarray)
        val line2 = line1.replace("group_array=()", garray)
        Some(line2)
      } else None
    } catch {
      case e: Throwable =>
        log.error("Could not open $fname, So the Right hadoop-policy.xml could not be generated")
        None
    }
  }

  def main(args: Array[String]): Unit = {
    val xx = getValidHadoopShell(List(DBUser(1, 1, "xx", "tuoyu"), DBUser(2, 10, "xx", "hello")), List(DBGroup(1, "test", "test"), DBGroup(2, "test2", "test2")))
    println(s" xx = $xx")
  }
}
