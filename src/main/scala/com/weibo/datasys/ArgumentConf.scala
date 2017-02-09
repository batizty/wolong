package com.weibo.datasys

import org.rogach.scallop.ScallopConf
import org.slf4j.LoggerFactory

/**
  * Created by tuoyu on 09/02/2017.
  */

class ArgumentConf(args: Seq[String])
  extends ScallopConf(args) {
  val log = LoggerFactory.getLogger(getClass.getName)
  version("wolong version 0.0.1")
  banner("""Usage: wolong""")
  footer("\nIf you met any question, please email to tuoyu@staff.weibo.com")

  val help = opt[Boolean](
    name = "help",
    default = Some(false),
    descr = "print this message",
    short = 'h',
    required = false
  )

  val debug_mode = opt[Boolean](
    name = "debug_mode",
    default = Some(false),
    short = 'X',
    hidden = true
  )

  val rest_service = opt[Boolean](noshort = true)

  val scheduler_service = opt[Boolean](noshort = true)

  /** sub commands example **/
  //  val jobs =  new Subcommand("jobs") {
  //    this.descr("jobs operations")
  //
  //    val list = opt[Boolean](
  //      descr = "list all Jobs summary information",
  //      required = false,
  //      default = Some(false),
  //      noshort = true
  //    )
  //
  //    val show = opt[String](
  //      descr = "show all Jobs detail",
  //      required = false,
  //      noshort = true
  //    )
  //
  //    val kill = opt[String](
  //      descr = "kill task of a Job",
  //      required = false,
  //      noshort = true
  //    )
  //
  //    val delete = opt[String](
  //      descr = "delete Job from Chronos",
  //      required = false,
  //      noshort = true
  //    )
  //  }
  //
  //  addSubcommand(jobs)

  override def verify() = {
    super.verify()
    if (rest_service.isEmpty &&
      scheduler_service.isEmpty) {
      log.error("No support module")
      this.printHelp()
      sys.exit(-1)
    }
  }

  verify()
}