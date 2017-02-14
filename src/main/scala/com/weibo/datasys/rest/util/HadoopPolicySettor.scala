package com.weibo.datasys.rest.util

import java.io.InputStream

import com.weibo.datasys.rest.data.{Group, User}
import org.slf4j.LoggerFactory

import scala.xml._

/**
 * Created by tuoyu on 04/02/2017.
 */
object HadoopPolicySettor {
  val log = LoggerFactory.getLogger(getClass.getName)

  val dfs_acl = "security.client.protocol.acl"
  val default_xml = "hadoop-policy.xml"
  val prefix =
    """<?xml version="1.0"?>
  <?xml-stylesheet type="text/xsl" href="configuration.xsl"?>
  <!--

 Licensed to the Apache Software Foundation (ASF) under one
 or more contributor license agreements.  See the NOTICE file
 distributed with this work for additional information
 regarding copyright ownership.  The ASF licenses this file
 to you under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License.  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 -->

 <!-- Put site-specific property overrides in this file.-->"""

  /** This Main is Just For Testing **/
  //  def main(args: Array[String]): Unit = {
  //    val ss = getValidHadoopPolicyXML(List.empty, List.empty, None)
  //    println(s"$ss")
  //    ()
  //  }

  def getValidHadoopPolicyXML(
    users: List[User],
    groups: List[Group],
    path: Option[String] = None
  ): Option[String] = {
    val fname = path.getOrElse(default_xml)
    try {
      val stream: InputStream = getClass.getResourceAsStream("/" + fname)
      val xml = XML.load(stream)
      val nvmap1 = (xml \ "property") map { node =>
        val n = (node \ "name").text.toString
        val v = (node \ "value").text.toString
        (n, v)
      } toMap

      val value = users.map(_.name.trim).mkString(",") +
        " " +
        groups.map(_.name.trim).mkString(",")

      val nvmap2 = nvmap1.filterNot {
        case (k, v) =>
          k == dfs_acl
      } ++ Map((dfs_acl, value))

      val xml2 =
        <configuration>
          { nvmap2 map { x => updateXMLFile(x) } }
        </configuration>

      val xmlWidth = 160
      val xmlStep = 4
      val p = new PrettyPrinter(xmlWidth, xmlStep)
      Some(prefix + p.format(xml2))
    } catch {
      case e: Throwable =>
        log.error(s"Read $fname failed, So the Right hadoop-policy.xml could not be generated")
        None
    }
  }

  def updateXMLFile(e: (String, String)) = {
    val (n, v) = e
    <property>
      <name>
        { n }
      </name>
      <value>
        { v }
      </value>
    </property>

  }
}
