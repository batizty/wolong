package com.weibo.datasys.rest.util

import java.io.InputStream

import com.weibo.datasys.rest.data.{DBGroup, DBUser, Group, User}
import com.weibo.datasys.rest.util.HadoopPolicySettor._
import org.slf4j.LoggerFactory

import scala.io.Source
import scala.util.Try

/**
 * Created by tuoyu on 04/02/2017.
 */
object HadoopShellSettor {
  val log = LoggerFactory.getLogger(getClass.getName)

  val default_shell = "add_user_to_hadoop.sh"

  /** This Main is Just for Testing **/
  //  def main(args: Array[String]): Unit = {
  //    val xx = getValidHadoopShell(List(DBUser(1, 1, "xx", "tuoyu"), DBUser(2, 10, "xx", "hello")), List(DBGroup(1, "test", "test"), DBGroup(2, "test2", "test2")))
  //    println(s" xx = $xx")
  //  }

  def getValidHadoopShell(
    users: List[User],
    groups: List[Group],
    path: Option[String] = None
  ): Option[String] = {
    val fname = path.getOrElse(default_shell)
    try {
      val stream: InputStream = getClass.getResourceAsStream("/" + fname)
      val lines = Source.fromInputStream(stream).mkString
      val gmap = groups
        .map { g => (g.groupId, g.groupName.trim) }
        .toMap
      val ug = users.map { u =>
        Try {
          gmap.get(u.userGroupId).map(gn => (u.name.trim, gn))
        }.getOrElse(None)
      }.flatten

      if (ug.nonEmpty) {
        val uarray = "user_array=(" + ug.map(_._1.trim).mkString(" ") + ")"
        val garray = "group_array=(" + ug.map(_._2.trim).mkString(" ") + ")"
        val line1 = lines.replace("user_array=()", uarray)
        val line2 = line1.replace("group_array=()", garray)
        Some(line2)
      } else {
        None
      }
    } catch {
      case e: Throwable =>
        log.error(s"Could not open $fname, So the Right $default_shell could not be generated, detail : ${e.getStackTrace}")
        throw e
        None
    }
  }
}
