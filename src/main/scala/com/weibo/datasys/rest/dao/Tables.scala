package com.weibo.datasys.rest.dao
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.jdbc.MySQLProfile
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Array(MmDataMining.schema, MmDataVisitorInteraction.schema, MmDataVisitorRemain.schema, MmDataVisitorUvPv.schema, MmFlume.schema, MmGroup.schema, MmKafka.schema, MmSession.schema, MmStormTag.schema, MmTrigger.schema, MmUser.schema, MmWeidisMetrics.schema, MmWgis.schema, MmWgisMetrics.schema, Testdatax.schema, Testdatax1.schema).reduceLeft(_ ++ _)

  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** Collection-like TableQuery object for table MmDataMining */
  lazy val MmDataMining = new TableQuery(tag => new MmDataMining(tag))
  /** Collection-like TableQuery object for table MmDataVisitorInteraction */
  lazy val MmDataVisitorInteraction = new TableQuery(tag => new MmDataVisitorInteraction(tag))
  /** Collection-like TableQuery object for table MmDataVisitorRemain */
  lazy val MmDataVisitorRemain = new TableQuery(tag => new MmDataVisitorRemain(tag))
  /** Collection-like TableQuery object for table MmDataVisitorUvPv */
  lazy val MmDataVisitorUvPv = new TableQuery(tag => new MmDataVisitorUvPv(tag))
  /** Collection-like TableQuery object for table MmFlume */
  lazy val MmFlume = new TableQuery(tag => new MmFlume(tag))
  /** Collection-like TableQuery object for table MmGroup */
  lazy val MmGroup = new TableQuery(tag => new MmGroup(tag))
  /** Collection-like TableQuery object for table MmKafka */
  lazy val MmKafka = new TableQuery(tag => new MmKafka(tag))
  /** Collection-like TableQuery object for table MmSession */
  lazy val MmSession = new TableQuery(tag => new MmSession(tag))
  /** Collection-like TableQuery object for table MmStormTag */
  lazy val MmStormTag = new TableQuery(tag => new MmStormTag(tag))
  /** Collection-like TableQuery object for table MmTrigger */
  lazy val MmTrigger = new TableQuery(tag => new MmTrigger(tag))
  /** Collection-like TableQuery object for table MmUser */
  lazy val MmUser = new TableQuery(tag => new MmUser(tag))
  /** Collection-like TableQuery object for table MmWeidisMetrics */
  lazy val MmWeidisMetrics = new TableQuery(tag => new MmWeidisMetrics(tag))
  /** Collection-like TableQuery object for table MmWgis */
  lazy val MmWgis = new TableQuery(tag => new MmWgis(tag))
  /** Collection-like TableQuery object for table MmWgisMetrics */
  lazy val MmWgisMetrics = new TableQuery(tag => new MmWgisMetrics(tag))
  /** Collection-like TableQuery object for table Testdatax */
  lazy val Testdatax = new TableQuery(tag => new Testdatax(tag))
  /** Collection-like TableQuery object for table Testdatax1 */
  lazy val Testdatax1 = new TableQuery(tag => new Testdatax1(tag))
  val profile: slick.jdbc.JdbcProfile

  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** GetResult implicit for fetching MmDataMiningRow objects using plain SQL queries */
  implicit def GetResultMmDataMiningRow(implicit e0: GR[Long], e1: GR[Option[java.sql.Timestamp]], e2: GR[Option[Long]], e3: GR[Int], e4: GR[Option[String]]): GR[MmDataMiningRow] = GR {
    prs =>
      import prs._
      MmDataMiningRow.tupled((<<[Long], <<?[java.sql.Timestamp], <<?[Long], <<[Int], <<?[String], <<?[Long]))
  }

  /** GetResult implicit for fetching MmDataVisitorInteractionRow objects using plain SQL queries */
  implicit def GetResultMmDataVisitorInteractionRow(implicit e0: GR[Long], e1: GR[Int], e2: GR[Option[Long]], e3: GR[Option[java.sql.Timestamp]]): GR[MmDataVisitorInteractionRow] = GR {
    prs =>
      import prs._
      MmDataVisitorInteractionRow.tupled((<<[Long], <<[Int], <<?[Long], <<?[java.sql.Timestamp], <<?[java.sql.Timestamp], <<[Int], <<[Int]))
  }

  /** GetResult implicit for fetching MmDataVisitorRemainRow objects using plain SQL queries */
  implicit def GetResultMmDataVisitorRemainRow(implicit e0: GR[Long], e1: GR[Option[java.sql.Timestamp]], e2: GR[Option[Long]], e3: GR[Int]): GR[MmDataVisitorRemainRow] = GR {
    prs =>
      import prs._
      MmDataVisitorRemainRow.tupled((<<[Long], <<?[java.sql.Timestamp], <<?[java.sql.Timestamp], <<?[Long], <<[Int], <<[Int], <<[Int], <<?[Long]))
  }

  /** GetResult implicit for fetching MmDataVisitorUvPvRow objects using plain SQL queries */
  implicit def GetResultMmDataVisitorUvPvRow(implicit e0: GR[Long], e1: GR[Option[java.sql.Timestamp]], e2: GR[Option[Long]], e3: GR[Int]): GR[MmDataVisitorUvPvRow] = GR {
    prs =>
      import prs._
      MmDataVisitorUvPvRow.tupled((<<[Long], <<?[java.sql.Timestamp], <<?[java.sql.Timestamp], <<?[Long], <<[Int], <<[Int], <<?[Long]))
  }

  /** GetResult implicit for fetching MmFlumeRow objects using plain SQL queries */
  implicit def GetResultMmFlumeRow(implicit e0: GR[Long], e1: GR[Option[java.sql.Timestamp]], e2: GR[Option[Long]], e3: GR[Int], e4: GR[Option[String]]): GR[MmFlumeRow] = GR {
    prs =>
      import prs._
      MmFlumeRow.tupled((<<[Long], <<?[java.sql.Timestamp], <<?[Long], <<[Int], <<?[String], <<?[Long]))
  }

  /** GetResult implicit for fetching MmGroupRow objects using plain SQL queries */
  implicit def GetResultMmGroupRow(implicit e0: GR[Long], e1: GR[Option[String]], e2: GR[Option[java.sql.Timestamp]]): GR[MmGroupRow] = GR {
    prs =>
      import prs._
      MmGroupRow.tupled((<<[Long], <<?[String], <<?[java.sql.Timestamp], <<?[String], <<?[String]))
  }

  /** GetResult implicit for fetching MmKafkaRow objects using plain SQL queries */
  implicit def GetResultMmKafkaRow(implicit e0: GR[Long], e1: GR[Option[java.sql.Timestamp]], e2: GR[Option[Long]], e3: GR[Int], e4: GR[Option[String]]): GR[MmKafkaRow] = GR {
    prs =>
      import prs._
      MmKafkaRow.tupled((<<[Long], <<?[java.sql.Timestamp], <<?[Long], <<?[Long], <<[Int], <<?[String]))
  }

  /** GetResult implicit for fetching MmSessionRow objects using plain SQL queries */
  implicit def GetResultMmSessionRow(implicit e0: GR[Long], e1: GR[Option[java.sql.Timestamp]], e2: GR[Int], e3: GR[Option[String]]): GR[MmSessionRow] = GR {
    prs =>
      import prs._
      MmSessionRow.tupled((<<[Long], <<?[java.sql.Timestamp], <<?[java.sql.Timestamp], <<[Long], <<[Int], <<?[String], <<[Long], <<?[String]))
  }

  /** GetResult implicit for fetching MmStormTagRow objects using plain SQL queries */
  implicit def GetResultMmStormTagRow(implicit e0: GR[Long], e1: GR[Option[java.sql.Timestamp]], e2: GR[Option[Long]], e3: GR[Int], e4: GR[Option[String]]): GR[MmStormTagRow] = GR {
    prs =>
      import prs._
      MmStormTagRow.tupled((<<[Long], <<?[java.sql.Timestamp], <<?[Long], <<[Int], <<?[String], <<?[Long], <<?[Long]))
  }

  /** GetResult implicit for fetching MmTriggerRow objects using plain SQL queries */
  implicit def GetResultMmTriggerRow(implicit e0: GR[Long], e1: GR[Option[java.sql.Timestamp]], e2: GR[Option[Long]], e3: GR[Int], e4: GR[Option[String]]): GR[MmTriggerRow] = GR {
    prs =>
      import prs._
      MmTriggerRow.tupled((<<[Long], <<?[java.sql.Timestamp], <<?[Long], <<?[Long], <<[Int], <<?[String]))
  }

  /** GetResult implicit for fetching MmUserRow objects using plain SQL queries */
  implicit def GetResultMmUserRow(implicit e0: GR[Long], e1: GR[Option[java.sql.Timestamp]], e2: GR[Int], e3: GR[Option[String]]): GR[MmUserRow] = GR {
    prs =>
      import prs._
      MmUserRow.tupled((<<[Long], <<?[java.sql.Timestamp], <<[Long], <<[Int], <<?[java.sql.Timestamp], <<?[String], <<?[String], <<?[String]))
  }

  /** GetResult implicit for fetching MmWeidisMetricsRow objects using plain SQL queries */
  implicit def GetResultMmWeidisMetricsRow(implicit e0: GR[Long], e1: GR[Option[Double]], e2: GR[Option[String]], e3: GR[Option[java.sql.Timestamp]]): GR[MmWeidisMetricsRow] = GR {
    prs =>
      import prs._
      MmWeidisMetricsRow.tupled((<<[Long], <<?[Double], <<?[String], <<?[Double], <<?[String], <<?[java.sql.Timestamp], <<?[String]))
  }

  /** GetResult implicit for fetching MmWgisRow objects using plain SQL queries */
  implicit def GetResultMmWgisRow(implicit e0: GR[Long], e1: GR[Option[java.sql.Timestamp]], e2: GR[Option[String]], e3: GR[Option[Double]]): GR[MmWgisRow] = GR {
    prs =>
      import prs._
      MmWgisRow.tupled((<<[Long], <<?[java.sql.Timestamp], <<?[String], <<?[Double], <<?[String], <<?[Double], <<?[Double], <<?[String]))
  }

  /** GetResult implicit for fetching MmWgisMetricsRow objects using plain SQL queries */
  implicit def GetResultMmWgisMetricsRow(implicit e0: GR[Long], e1: GR[Option[Double]], e2: GR[Option[String]], e3: GR[Option[java.sql.Timestamp]]): GR[MmWgisMetricsRow] = GR {
    prs =>
      import prs._
      MmWgisMetricsRow.tupled((<<[Long], <<?[Double], <<?[String], <<?[Double], <<?[String], <<?[java.sql.Timestamp], <<?[String]))
  }

  /** GetResult implicit for fetching TestdataxRow objects using plain SQL queries */
  implicit def GetResultTestdataxRow(implicit e0: GR[Option[String]]): GR[TestdataxRow] = GR {
    prs =>
      import prs._
      TestdataxRow.tupled((<<?[String], <<?[String]))
  }

  /** GetResult implicit for fetching Testdatax1Row objects using plain SQL queries */
  implicit def GetResultTestdatax1Row(implicit e0: GR[Option[String]], e1: GR[Option[Double]]): GR[Testdatax1Row] = GR {
    prs =>
      import prs._
      Testdatax1Row.tupled((<<?[String], <<?[Double]))
  }

  /**
    * Entity class storing rows of table MmDataMining
    *
    *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param createdate Database column createDate SqlType(DATETIME), Default(None)
   *  @param totalcount Database column totalCount SqlType(BIGINT), Default(None)
   *  @param `type` Database column type SqlType(INT)
   *  @param typedesc Database column typeDesc SqlType(VARCHAR), Length(255,true), Default(None)
    * @param totalresultcount Database column totalResultCount SqlType(BIGINT), Default(None)
    */
  case class MmDataMiningRow(id: Long, createdate: Option[java.sql.Timestamp] = None, totalcount: Option[Long] = None, `type`: Int, typedesc: Option[String] = None, totalresultcount: Option[Long] = None)

  /**
    * Table description of table mm_data_mining. Objects of this class serve as prototypes for rows in queries.
    * NOTE: The following names collided with Scala keywords and were escaped: type
    */
  class MmDataMining(_tableTag: Tag) extends profile.api.Table[MmDataMiningRow](_tableTag, Some("datasys_monitor"), "mm_data_mining") {
    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column createDate SqlType(DATETIME), Default(None) */
    val createdate: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("createDate", O.Default(None))
    /** Database column totalCount SqlType(BIGINT), Default(None) */
    val totalcount: Rep[Option[Long]] = column[Option[Long]]("totalCount", O.Default(None))
    /**
      * Database column type SqlType(INT)
      * NOTE: The name was escaped because it collided with a Scala keyword.
      */
    val `type`: Rep[Int] = column[Int]("type")
    /** Database column typeDesc SqlType(VARCHAR), Length(255,true), Default(None) */
    val typedesc: Rep[Option[String]] = column[Option[String]]("typeDesc", O.Length(255, varying = true), O.Default(None))
    /** Database column totalResultCount SqlType(BIGINT), Default(None) */
    val totalresultcount: Rep[Option[Long]] = column[Option[Long]]("totalResultCount", O.Default(None))

    def * = (id, createdate, totalcount, `type`, typedesc, totalresultcount) <>(MmDataMiningRow.tupled, MmDataMiningRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), createdate, totalcount, Rep.Some(`type`), typedesc, totalresultcount).shaped.<>({ r =>; _1.map(_ => MmDataMiningRow.tupled((_1.get, _2, _3, _4.get, _5, _6))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))
  }

  /**
    * Entity class storing rows of table MmDataVisitorInteraction
    *
    *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param action Database column action SqlType(INT)
   *  @param count Database column count SqlType(BIGINT), Default(None)
   *  @param countdate Database column countDate SqlType(DATETIME), Default(None)
   *  @param createdate Database column createDate SqlType(DATETIME), Default(None)
   *  @param uidTailType Database column uid_tail_type SqlType(INT)
    * @param userType Database column user_type SqlType(INT)
   */
  case class MmDataVisitorInteractionRow(id: Long, action: Int, count: Option[Long] = None, countdate: Option[java.sql.Timestamp] = None, createdate: Option[java.sql.Timestamp] = None, uidTailType: Int, userType: Int)

  /** Table description of table mm_data_visitor_interaction. Objects of this class serve as prototypes for rows in queries. */
  class MmDataVisitorInteraction(_tableTag: Tag) extends profile.api.Table[MmDataVisitorInteractionRow](_tableTag, Some("datasys_monitor"), "mm_data_visitor_interaction") {
    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column action SqlType(INT) */
    val action: Rep[Int] = column[Int]("action")
    /** Database column count SqlType(BIGINT), Default(None) */
    val count: Rep[Option[Long]] = column[Option[Long]]("count", O.Default(None))
    /** Database column countDate SqlType(DATETIME), Default(None) */
    val countdate: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("countDate", O.Default(None))
    /** Database column createDate SqlType(DATETIME), Default(None) */
    val createdate: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("createDate", O.Default(None))
    /** Database column uid_tail_type SqlType(INT) */
    val uidTailType: Rep[Int] = column[Int]("uid_tail_type")
    /** Database column user_type SqlType(INT) */
    val userType: Rep[Int] = column[Int]("user_type")

    def * = (id, action, count, countdate, createdate, uidTailType, userType) <>(MmDataVisitorInteractionRow.tupled, MmDataVisitorInteractionRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(action), count, countdate, createdate, Rep.Some(uidTailType), Rep.Some(userType)).shaped.<>({ r =>; _1.map(_ => MmDataVisitorInteractionRow.tupled((_1.get, _2.get, _3, _4, _5, _6.get, _7.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))
  }

  /**
    * Entity class storing rows of table MmDataVisitorRemain
    *
    *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param countdate Database column countDate SqlType(DATETIME), Default(None)
   *  @param createdate Database column createDate SqlType(DATETIME), Default(None)
   *  @param pvcount Database column pvCount SqlType(BIGINT), Default(None)
   *  @param remainDaysType Database column remain_days_type SqlType(INT)
   *  @param uidTailType Database column uid_tail_type SqlType(INT)
   *  @param userType Database column user_type SqlType(INT)
    * @param uvcount Database column uvCount SqlType(BIGINT), Default(None)
   */
  case class MmDataVisitorRemainRow(id: Long, countdate: Option[java.sql.Timestamp] = None, createdate: Option[java.sql.Timestamp] = None, pvcount: Option[Long] = None, remainDaysType: Int, uidTailType: Int, userType: Int, uvcount: Option[Long] = None)

  /** Table description of table mm_data_visitor_remain. Objects of this class serve as prototypes for rows in queries. */
  class MmDataVisitorRemain(_tableTag: Tag) extends profile.api.Table[MmDataVisitorRemainRow](_tableTag, Some("datasys_monitor"), "mm_data_visitor_remain") {
    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column countDate SqlType(DATETIME), Default(None) */
    val countdate: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("countDate", O.Default(None))
    /** Database column createDate SqlType(DATETIME), Default(None) */
    val createdate: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("createDate", O.Default(None))
    /** Database column pvCount SqlType(BIGINT), Default(None) */
    val pvcount: Rep[Option[Long]] = column[Option[Long]]("pvCount", O.Default(None))
    /** Database column remain_days_type SqlType(INT) */
    val remainDaysType: Rep[Int] = column[Int]("remain_days_type")
    /** Database column uid_tail_type SqlType(INT) */
    val uidTailType: Rep[Int] = column[Int]("uid_tail_type")
    /** Database column user_type SqlType(INT) */
    val userType: Rep[Int] = column[Int]("user_type")
    /** Database column uvCount SqlType(BIGINT), Default(None) */
    val uvcount: Rep[Option[Long]] = column[Option[Long]]("uvCount", O.Default(None))

    def * = (id, countdate, createdate, pvcount, remainDaysType, uidTailType, userType, uvcount) <>(MmDataVisitorRemainRow.tupled, MmDataVisitorRemainRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), countdate, createdate, pvcount, Rep.Some(remainDaysType), Rep.Some(uidTailType), Rep.Some(userType), uvcount).shaped.<>({ r =>; _1.map(_ => MmDataVisitorRemainRow.tupled((_1.get, _2, _3, _4, _5.get, _6.get, _7.get, _8))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))
  }

  /**
    * Entity class storing rows of table MmDataVisitorUvPv
    *
    *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param countdate Database column countDate SqlType(DATETIME), Default(None)
   *  @param createdate Database column createDate SqlType(DATETIME), Default(None)
   *  @param pvcount Database column pvCount SqlType(BIGINT), Default(None)
   *  @param uidTailType Database column uid_tail_type SqlType(INT)
   *  @param userType Database column user_type SqlType(INT)
    * @param uvcount Database column uvCount SqlType(BIGINT), Default(None)
   */
  case class MmDataVisitorUvPvRow(id: Long, countdate: Option[java.sql.Timestamp] = None, createdate: Option[java.sql.Timestamp] = None, pvcount: Option[Long] = None, uidTailType: Int, userType: Int, uvcount: Option[Long] = None)

  /** Table description of table mm_data_visitor_uv_pv. Objects of this class serve as prototypes for rows in queries. */
  class MmDataVisitorUvPv(_tableTag: Tag) extends profile.api.Table[MmDataVisitorUvPvRow](_tableTag, Some("datasys_monitor"), "mm_data_visitor_uv_pv") {
    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column countDate SqlType(DATETIME), Default(None) */
    val countdate: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("countDate", O.Default(None))
    /** Database column createDate SqlType(DATETIME), Default(None) */
    val createdate: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("createDate", O.Default(None))
    /** Database column pvCount SqlType(BIGINT), Default(None) */
    val pvcount: Rep[Option[Long]] = column[Option[Long]]("pvCount", O.Default(None))
    /** Database column uid_tail_type SqlType(INT) */
    val uidTailType: Rep[Int] = column[Int]("uid_tail_type")
    /** Database column user_type SqlType(INT) */
    val userType: Rep[Int] = column[Int]("user_type")
    /** Database column uvCount SqlType(BIGINT), Default(None) */
    val uvcount: Rep[Option[Long]] = column[Option[Long]]("uvCount", O.Default(None))

    def * = (id, countdate, createdate, pvcount, uidTailType, userType, uvcount) <>(MmDataVisitorUvPvRow.tupled, MmDataVisitorUvPvRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), countdate, createdate, pvcount, Rep.Some(uidTailType), Rep.Some(userType), uvcount).shaped.<>({ r =>; _1.map(_ => MmDataVisitorUvPvRow.tupled((_1.get, _2, _3, _4, _5.get, _6.get, _7))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))
  }

  /**
    * Entity class storing rows of table MmFlume
    *
    *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param createdate Database column createDate SqlType(DATETIME), Default(None)
   *  @param totalcount Database column totalCount SqlType(BIGINT), Default(None)
   *  @param `type` Database column type SqlType(INT)
   *  @param typedesc Database column typeDesc SqlType(VARCHAR), Length(255,true), Default(None)
    * @param totalnewcount Database column totalNewCount SqlType(BIGINT), Default(None)
   */
  case class MmFlumeRow(id: Long, createdate: Option[java.sql.Timestamp] = None, totalcount: Option[Long] = None, `type`: Int, typedesc: Option[String] = None, totalnewcount: Option[Long] = None)

  /**
    * Table description of table mm_flume. Objects of this class serve as prototypes for rows in queries.
    * NOTE: The following names collided with Scala keywords and were escaped: type
    */
  class MmFlume(_tableTag: Tag) extends profile.api.Table[MmFlumeRow](_tableTag, Some("datasys_monitor"), "mm_flume") {
    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column createDate SqlType(DATETIME), Default(None) */
    val createdate: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("createDate", O.Default(None))
    /** Database column totalCount SqlType(BIGINT), Default(None) */
    val totalcount: Rep[Option[Long]] = column[Option[Long]]("totalCount", O.Default(None))
    /**
      * Database column type SqlType(INT)
      * NOTE: The name was escaped because it collided with a Scala keyword.
     */
    val `type`: Rep[Int] = column[Int]("type")
    /** Database column typeDesc SqlType(VARCHAR), Length(255,true), Default(None) */
    val typedesc: Rep[Option[String]] = column[Option[String]]("typeDesc", O.Length(255, varying = true), O.Default(None))
    /** Database column totalNewCount SqlType(BIGINT), Default(None) */
    val totalnewcount: Rep[Option[Long]] = column[Option[Long]]("totalNewCount", O.Default(None))

    def * = (id, createdate, totalcount, `type`, typedesc, totalnewcount) <>(MmFlumeRow.tupled, MmFlumeRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), createdate, totalcount, Rep.Some(`type`), typedesc, totalnewcount).shaped.<>({ r =>; _1.map(_ => MmFlumeRow.tupled((_1.get, _2, _3, _4.get, _5, _6))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))
  }

  /**
    * Entity class storing rows of table MmGroup
    *
    *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param cataloguename Database column catalogueName SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param createat Database column createAt SqlType(DATETIME), Default(None)
   *  @param creator Database column creator SqlType(VARCHAR), Length(255,true), Default(None)
    * @param groupname Database column groupName SqlType(VARCHAR), Length(255,true), Default(None)
   */
  case class MmGroupRow(id: Long, cataloguename: Option[String] = None, createat: Option[java.sql.Timestamp] = None, creator: Option[String] = None, groupname: Option[String] = None)

  /** Table description of table mm_group. Objects of this class serve as prototypes for rows in queries. */
  class MmGroup(_tableTag: Tag) extends profile.api.Table[MmGroupRow](_tableTag, Some("datasys_monitor"), "mm_group") {
    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column catalogueName SqlType(VARCHAR), Length(255,true), Default(None) */
    val cataloguename: Rep[Option[String]] = column[Option[String]]("catalogueName", O.Length(255, varying = true), O.Default(None))
    /** Database column createAt SqlType(DATETIME), Default(None) */
    val createat: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("createAt", O.Default(None))
    /** Database column creator SqlType(VARCHAR), Length(255,true), Default(None) */
    val creator: Rep[Option[String]] = column[Option[String]]("creator", O.Length(255, varying = true), O.Default(None))
    /** Database column groupName SqlType(VARCHAR), Length(255,true), Default(None) */
    val groupname: Rep[Option[String]] = column[Option[String]]("groupName", O.Length(255, varying = true), O.Default(None))

    def * = (id, cataloguename, createat, creator, groupname) <>(MmGroupRow.tupled, MmGroupRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), cataloguename, createat, creator, groupname).shaped.<>({ r =>; _1.map(_ => MmGroupRow.tupled((_1.get, _2, _3, _4, _5))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))
  }

  /**
    * Entity class storing rows of table MmKafka
    *
    *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param createdate Database column createDate SqlType(DATETIME), Default(None)
   *  @param notconsumecount Database column notConsumeCount SqlType(BIGINT), Default(None)
   *  @param totalcount Database column totalCount SqlType(BIGINT), Default(None)
   *  @param `type` Database column type SqlType(INT)
    * @param typedesc Database column typeDesc SqlType(VARCHAR), Length(255,true), Default(None)
   */
  case class MmKafkaRow(id: Long, createdate: Option[java.sql.Timestamp] = None, notconsumecount: Option[Long] = None, totalcount: Option[Long] = None, `type`: Int, typedesc: Option[String] = None)

  /**
    * Table description of table mm_kafka. Objects of this class serve as prototypes for rows in queries.
    * NOTE: The following names collided with Scala keywords and were escaped: type
    */
  class MmKafka(_tableTag: Tag) extends profile.api.Table[MmKafkaRow](_tableTag, Some("datasys_monitor"), "mm_kafka") {
    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column createDate SqlType(DATETIME), Default(None) */
    val createdate: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("createDate", O.Default(None))
    /** Database column notConsumeCount SqlType(BIGINT), Default(None) */
    val notconsumecount: Rep[Option[Long]] = column[Option[Long]]("notConsumeCount", O.Default(None))
    /** Database column totalCount SqlType(BIGINT), Default(None) */
    val totalcount: Rep[Option[Long]] = column[Option[Long]]("totalCount", O.Default(None))
    /**
      * Database column type SqlType(INT)
      * NOTE: The name was escaped because it collided with a Scala keyword.
     */
    val `type`: Rep[Int] = column[Int]("type")
    /** Database column typeDesc SqlType(VARCHAR), Length(255,true), Default(None) */
    val typedesc: Rep[Option[String]] = column[Option[String]]("typeDesc", O.Length(255, varying = true), O.Default(None))

    def * = (id, createdate, notconsumecount, totalcount, `type`, typedesc) <>(MmKafkaRow.tupled, MmKafkaRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), createdate, notconsumecount, totalcount, Rep.Some(`type`), typedesc).shaped.<>({ r =>; _1.map(_ => MmKafkaRow.tupled((_1.get, _2, _3, _4, _5.get, _6))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))
  }

  /**
    * Entity class storing rows of table MmSession
    *
    *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param createat Database column createAt SqlType(DATETIME), Default(None)
   *  @param expireddate Database column expiredDate SqlType(DATETIME), Default(None)
   *  @param groupid Database column groupId SqlType(BIGINT)
   *  @param indentfy Database column indentfy SqlType(INT)
   *  @param token Database column token SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param userid Database column userId SqlType(BIGINT)
    * @param username Database column userName SqlType(VARCHAR), Length(255,true), Default(None)
   */
  case class MmSessionRow(id: Long, createat: Option[java.sql.Timestamp] = None, expireddate: Option[java.sql.Timestamp] = None, groupid: Long, indentfy: Int, token: Option[String] = None, userid: Long, username: Option[String] = None)

  /** Table description of table mm_session. Objects of this class serve as prototypes for rows in queries. */
  class MmSession(_tableTag: Tag) extends profile.api.Table[MmSessionRow](_tableTag, Some("datasys_monitor"), "mm_session") {
    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column createAt SqlType(DATETIME), Default(None) */
    val createat: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("createAt", O.Default(None))
    /** Database column expiredDate SqlType(DATETIME), Default(None) */
    val expireddate: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("expiredDate", O.Default(None))
    /** Database column groupId SqlType(BIGINT) */
    val groupid: Rep[Long] = column[Long]("groupId")
    /** Database column indentfy SqlType(INT) */
    val indentfy: Rep[Int] = column[Int]("indentfy")
    /** Database column token SqlType(VARCHAR), Length(255,true), Default(None) */
    val token: Rep[Option[String]] = column[Option[String]]("token", O.Length(255, varying = true), O.Default(None))
    /** Database column userId SqlType(BIGINT) */
    val userid: Rep[Long] = column[Long]("userId")
    /** Database column userName SqlType(VARCHAR), Length(255,true), Default(None) */
    val username: Rep[Option[String]] = column[Option[String]]("userName", O.Length(255, varying = true), O.Default(None))

    def * = (id, createat, expireddate, groupid, indentfy, token, userid, username) <>(MmSessionRow.tupled, MmSessionRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), createat, expireddate, Rep.Some(groupid), Rep.Some(indentfy), token, Rep.Some(userid), username).shaped.<>({ r =>; _1.map(_ => MmSessionRow.tupled((_1.get, _2, _3, _4.get, _5.get, _6, _7.get, _8))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))
  }

  /**
    * Entity class storing rows of table MmStormTag
    *
    *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param createdate Database column createDate SqlType(DATETIME), Default(None)
   *  @param totalcount Database column totalCount SqlType(BIGINT), Default(None)
   *  @param `type` Database column type SqlType(INT)
   *  @param typedesc Database column typeDesc SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param totaloriginalcount Database column totalOriginalCount SqlType(BIGINT), Default(None)
    * @param totalheadtagcount Database column totalHeadTagCount SqlType(BIGINT), Default(None)
   */
  case class MmStormTagRow(id: Long, createdate: Option[java.sql.Timestamp] = None, totalcount: Option[Long] = None, `type`: Int, typedesc: Option[String] = None, totaloriginalcount: Option[Long] = None, totalheadtagcount: Option[Long] = None)

  /**
    * Table description of table mm_storm_tag. Objects of this class serve as prototypes for rows in queries.
    * NOTE: The following names collided with Scala keywords and were escaped: type
    */
  class MmStormTag(_tableTag: Tag) extends profile.api.Table[MmStormTagRow](_tableTag, Some("datasys_monitor"), "mm_storm_tag") {
    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column createDate SqlType(DATETIME), Default(None) */
    val createdate: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("createDate", O.Default(None))
    /** Database column totalCount SqlType(BIGINT), Default(None) */
    val totalcount: Rep[Option[Long]] = column[Option[Long]]("totalCount", O.Default(None))
    /**
      * Database column type SqlType(INT)
      * NOTE: The name was escaped because it collided with a Scala keyword.
     */
    val `type`: Rep[Int] = column[Int]("type")
    /** Database column typeDesc SqlType(VARCHAR), Length(255,true), Default(None) */
    val typedesc: Rep[Option[String]] = column[Option[String]]("typeDesc", O.Length(255, varying = true), O.Default(None))
    /** Database column totalOriginalCount SqlType(BIGINT), Default(None) */
    val totaloriginalcount: Rep[Option[Long]] = column[Option[Long]]("totalOriginalCount", O.Default(None))
    /** Database column totalHeadTagCount SqlType(BIGINT), Default(None) */
    val totalheadtagcount: Rep[Option[Long]] = column[Option[Long]]("totalHeadTagCount", O.Default(None))

    def * = (id, createdate, totalcount, `type`, typedesc, totaloriginalcount, totalheadtagcount) <>(MmStormTagRow.tupled, MmStormTagRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), createdate, totalcount, Rep.Some(`type`), typedesc, totaloriginalcount, totalheadtagcount).shaped.<>({ r =>; _1.map(_ => MmStormTagRow.tupled((_1.get, _2, _3, _4.get, _5, _6, _7))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))
  }

  /**
    * Entity class storing rows of table MmTrigger
    *
    *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param createdate Database column createDate SqlType(DATETIME), Default(None)
   *  @param totalcount Database column totalCount SqlType(BIGINT), Default(None)
   *  @param totalresultcount Database column totalResultCount SqlType(BIGINT), Default(None)
   *  @param `type` Database column type SqlType(INT)
    * @param typedesc Database column typeDesc SqlType(VARCHAR), Length(255,true), Default(None)
   */
  case class MmTriggerRow(id: Long, createdate: Option[java.sql.Timestamp] = None, totalcount: Option[Long] = None, totalresultcount: Option[Long] = None, `type`: Int, typedesc: Option[String] = None)

  /**
    * Table description of table mm_trigger. Objects of this class serve as prototypes for rows in queries.
    * NOTE: The following names collided with Scala keywords and were escaped: type
    */
  class MmTrigger(_tableTag: Tag) extends profile.api.Table[MmTriggerRow](_tableTag, Some("datasys_monitor"), "mm_trigger") {
    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column createDate SqlType(DATETIME), Default(None) */
    val createdate: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("createDate", O.Default(None))
    /** Database column totalCount SqlType(BIGINT), Default(None) */
    val totalcount: Rep[Option[Long]] = column[Option[Long]]("totalCount", O.Default(None))
    /** Database column totalResultCount SqlType(BIGINT), Default(None) */
    val totalresultcount: Rep[Option[Long]] = column[Option[Long]]("totalResultCount", O.Default(None))
    /**
      * Database column type SqlType(INT)
      * NOTE: The name was escaped because it collided with a Scala keyword.
     */
    val `type`: Rep[Int] = column[Int]("type")
    /** Database column typeDesc SqlType(VARCHAR), Length(255,true), Default(None) */
    val typedesc: Rep[Option[String]] = column[Option[String]]("typeDesc", O.Length(255, varying = true), O.Default(None))

    def * = (id, createdate, totalcount, totalresultcount, `type`, typedesc) <>(MmTriggerRow.tupled, MmTriggerRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), createdate, totalcount, totalresultcount, Rep.Some(`type`), typedesc).shaped.<>({ r =>; _1.map(_ => MmTriggerRow.tupled((_1.get, _2, _3, _4, _5.get, _6))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))
  }

  /**
    * Entity class storing rows of table MmUser
    *
    *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param createat Database column createAt SqlType(DATETIME), Default(None)
   *  @param groupid Database column groupId SqlType(BIGINT)
   *  @param indentfy Database column indentfy SqlType(INT)
   *  @param lastlogintime Database column lastLoginTime SqlType(DATETIME), Default(None)
   *  @param password Database column password SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param state Database column state SqlType(VARCHAR), Length(255,true), Default(None)
    * @param username Database column userName SqlType(VARCHAR), Length(255,true), Default(None)
   */
  case class MmUserRow(id: Long, createat: Option[java.sql.Timestamp] = None, groupid: Long, indentfy: Int, lastlogintime: Option[java.sql.Timestamp] = None, password: Option[String] = None, state: Option[String] = None, username: Option[String] = None)

  /** Table description of table mm_user. Objects of this class serve as prototypes for rows in queries. */
  class MmUser(_tableTag: Tag) extends profile.api.Table[MmUserRow](_tableTag, Some("datasys_monitor"), "mm_user") {
    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column createAt SqlType(DATETIME), Default(None) */
    val createat: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("createAt", O.Default(None))
    /** Database column groupId SqlType(BIGINT) */
    val groupid: Rep[Long] = column[Long]("groupId")
    /** Database column indentfy SqlType(INT) */
    val indentfy: Rep[Int] = column[Int]("indentfy")
    /** Database column lastLoginTime SqlType(DATETIME), Default(None) */
    val lastlogintime: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("lastLoginTime", O.Default(None))
    /** Database column password SqlType(VARCHAR), Length(255,true), Default(None) */
    val password: Rep[Option[String]] = column[Option[String]]("password", O.Length(255, varying = true), O.Default(None))
    /** Database column state SqlType(VARCHAR), Length(255,true), Default(None) */
    val state: Rep[Option[String]] = column[Option[String]]("state", O.Length(255, varying = true), O.Default(None))
    /** Database column userName SqlType(VARCHAR), Length(255,true), Default(None) */
    val username: Rep[Option[String]] = column[Option[String]]("userName", O.Length(255, varying = true), O.Default(None))
    /** Uniqueness Index over (username) (database name userName) */
    val index1 = index("userName", username, unique = true)

    def * = (id, createat, groupid, indentfy, lastlogintime, password, state, username) <>(MmUserRow.tupled, MmUserRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), createat, Rep.Some(groupid), Rep.Some(indentfy), lastlogintime, password, state, username).shaped.<>({ r =>; _1.map(_ => MmUserRow.tupled((_1.get, _2, _3.get, _4.get, _5, _6, _7, _8))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))
  }

  /**
    * Entity class storing rows of table MmWeidisMetrics
    *
    *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param averageresponsetime Database column averageResponseTime SqlType(DOUBLE), Default(None)
   *  @param host Database column host SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param queriespersecond Database column queriesPerSecond SqlType(DOUBLE), Default(None)
   *  @param shard Database column shard SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param timestamp Database column timeStamp SqlType(DATETIME), Default(None)
    * @param vertical Database column vertical SqlType(VARCHAR), Length(255,true), Default(None)
   */
  case class MmWeidisMetricsRow(id: Long, averageresponsetime: Option[Double] = None, host: Option[String] = None, queriespersecond: Option[Double] = None, shard: Option[String] = None, timestamp: Option[java.sql.Timestamp] = None, vertical: Option[String] = None)

  /** Table description of table mm_weidis_metrics. Objects of this class serve as prototypes for rows in queries. */
  class MmWeidisMetrics(_tableTag: Tag) extends profile.api.Table[MmWeidisMetricsRow](_tableTag, Some("datasys_monitor"), "mm_weidis_metrics") {
    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column averageResponseTime SqlType(DOUBLE), Default(None) */
    val averageresponsetime: Rep[Option[Double]] = column[Option[Double]]("averageResponseTime", O.Default(None))
    /** Database column host SqlType(VARCHAR), Length(255,true), Default(None) */
    val host: Rep[Option[String]] = column[Option[String]]("host", O.Length(255, varying = true), O.Default(None))
    /** Database column queriesPerSecond SqlType(DOUBLE), Default(None) */
    val queriespersecond: Rep[Option[Double]] = column[Option[Double]]("queriesPerSecond", O.Default(None))
    /** Database column shard SqlType(VARCHAR), Length(255,true), Default(None) */
    val shard: Rep[Option[String]] = column[Option[String]]("shard", O.Length(255, varying = true), O.Default(None))
    /** Database column timeStamp SqlType(DATETIME), Default(None) */
    val timestamp: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("timeStamp", O.Default(None))
    /** Database column vertical SqlType(VARCHAR), Length(255,true), Default(None) */
    val vertical: Rep[Option[String]] = column[Option[String]]("vertical", O.Length(255, varying = true), O.Default(None))

    def * = (id, averageresponsetime, host, queriespersecond, shard, timestamp, vertical) <>(MmWeidisMetricsRow.tupled, MmWeidisMetricsRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), averageresponsetime, host, queriespersecond, shard, timestamp, vertical).shaped.<>({ r =>; _1.map(_ => MmWeidisMetricsRow.tupled((_1.get, _2, _3, _4, _5, _6, _7))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))
  }

  /**
    * Entity class storing rows of table MmWgis
    *
    *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param createdate Database column createDate SqlType(DATETIME), Default(None)
   *  @param host Database column host SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param querycount Database column querycount SqlType(DOUBLE), Default(None)
   *  @param shard Database column shard SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param timecount Database column timecount SqlType(DOUBLE), Default(None)
   *  @param timerange Database column timerange SqlType(DOUBLE), Default(None)
    * @param vertical Database column vertical SqlType(VARCHAR), Length(255,true), Default(None)
   */
  case class MmWgisRow(id: Long, createdate: Option[java.sql.Timestamp] = None, host: Option[String] = None, querycount: Option[Double] = None, shard: Option[String] = None, timecount: Option[Double] = None, timerange: Option[Double] = None, vertical: Option[String] = None)

  /** Table description of table mm_wgis. Objects of this class serve as prototypes for rows in queries. */
  class MmWgis(_tableTag: Tag) extends profile.api.Table[MmWgisRow](_tableTag, Some("datasys_monitor"), "mm_wgis") {
    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column createDate SqlType(DATETIME), Default(None) */
    val createdate: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("createDate", O.Default(None))
    /** Database column host SqlType(VARCHAR), Length(255,true), Default(None) */
    val host: Rep[Option[String]] = column[Option[String]]("host", O.Length(255, varying = true), O.Default(None))
    /** Database column querycount SqlType(DOUBLE), Default(None) */
    val querycount: Rep[Option[Double]] = column[Option[Double]]("querycount", O.Default(None))
    /** Database column shard SqlType(VARCHAR), Length(255,true), Default(None) */
    val shard: Rep[Option[String]] = column[Option[String]]("shard", O.Length(255, varying = true), O.Default(None))
    /** Database column timecount SqlType(DOUBLE), Default(None) */
    val timecount: Rep[Option[Double]] = column[Option[Double]]("timecount", O.Default(None))
    /** Database column timerange SqlType(DOUBLE), Default(None) */
    val timerange: Rep[Option[Double]] = column[Option[Double]]("timerange", O.Default(None))
    /** Database column vertical SqlType(VARCHAR), Length(255,true), Default(None) */
    val vertical: Rep[Option[String]] = column[Option[String]]("vertical", O.Length(255, varying = true), O.Default(None))

    def * = (id, createdate, host, querycount, shard, timecount, timerange, vertical) <>(MmWgisRow.tupled, MmWgisRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), createdate, host, querycount, shard, timecount, timerange, vertical).shaped.<>({ r =>; _1.map(_ => MmWgisRow.tupled((_1.get, _2, _3, _4, _5, _6, _7, _8))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))
  }

  /**
    * Entity class storing rows of table MmWgisMetrics
    *
    *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param averageresponsetime Database column averageResponseTime SqlType(DOUBLE), Default(None)
   *  @param host Database column host SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param queriespersecond Database column queriesPerSecond SqlType(DOUBLE), Default(None)
   *  @param shard Database column shard SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param timestamp Database column timeStamp SqlType(DATETIME), Default(None)
    * @param vertical Database column vertical SqlType(VARCHAR), Length(255,true), Default(None)
   */
  case class MmWgisMetricsRow(id: Long, averageresponsetime: Option[Double] = None, host: Option[String] = None, queriespersecond: Option[Double] = None, shard: Option[String] = None, timestamp: Option[java.sql.Timestamp] = None, vertical: Option[String] = None)

  /** Table description of table mm_wgis_metrics. Objects of this class serve as prototypes for rows in queries. */
  class MmWgisMetrics(_tableTag: Tag) extends profile.api.Table[MmWgisMetricsRow](_tableTag, Some("datasys_monitor"), "mm_wgis_metrics") {
    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column averageResponseTime SqlType(DOUBLE), Default(None) */
    val averageresponsetime: Rep[Option[Double]] = column[Option[Double]]("averageResponseTime", O.Default(None))
    /** Database column host SqlType(VARCHAR), Length(255,true), Default(None) */
    val host: Rep[Option[String]] = column[Option[String]]("host", O.Length(255, varying = true), O.Default(None))
    /** Database column queriesPerSecond SqlType(DOUBLE), Default(None) */
    val queriespersecond: Rep[Option[Double]] = column[Option[Double]]("queriesPerSecond", O.Default(None))
    /** Database column shard SqlType(VARCHAR), Length(255,true), Default(None) */
    val shard: Rep[Option[String]] = column[Option[String]]("shard", O.Length(255, varying = true), O.Default(None))
    /** Database column timeStamp SqlType(DATETIME), Default(None) */
    val timestamp: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("timeStamp", O.Default(None))
    /** Database column vertical SqlType(VARCHAR), Length(255,true), Default(None) */
    val vertical: Rep[Option[String]] = column[Option[String]]("vertical", O.Length(255, varying = true), O.Default(None))

    def * = (id, averageresponsetime, host, queriespersecond, shard, timestamp, vertical) <>(MmWgisMetricsRow.tupled, MmWgisMetricsRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), averageresponsetime, host, queriespersecond, shard, timestamp, vertical).shaped.<>({ r =>; _1.map(_ => MmWgisMetricsRow.tupled((_1.get, _2, _3, _4, _5, _6, _7))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))
  }

  /**
    * Entity class storing rows of table Testdatax
    *
    * @param username Database column userName SqlType(VARCHAR), Length(20,true), Default(None)
    * @param age      Database column age SqlType(MEDIUMTEXT), Length(16777215,true), Default(None)
   */
  case class TestdataxRow(username: Option[String] = None, age: Option[String] = None)

  /** Table description of table testDataX. Objects of this class serve as prototypes for rows in queries. */
  class Testdatax(_tableTag: Tag) extends profile.api.Table[TestdataxRow](_tableTag, Some("datasys_monitor"), "testDataX") {
    /** Database column userName SqlType(VARCHAR), Length(20,true), Default(None) */
    val username: Rep[Option[String]] = column[Option[String]]("userName", O.Length(20, varying = true), O.Default(None))
    /** Database column age SqlType(MEDIUMTEXT), Length(16777215,true), Default(None) */
    val age: Rep[Option[String]] = column[Option[String]]("age", O.Length(16777215, varying = true), O.Default(None))

    def * = (username, age) <>(TestdataxRow.tupled, TestdataxRow.unapply)
  }

  /**
    * Entity class storing rows of table Testdatax1
    *
    * @param username Database column userName SqlType(VARCHAR), Length(20,true), Default(None)
    * @param age      Database column age SqlType(DOUBLE), Default(None)
   */
  case class Testdatax1Row(username: Option[String] = None, age: Option[Double] = None)

  /** Table description of table testDataX1. Objects of this class serve as prototypes for rows in queries. */
  class Testdatax1(_tableTag: Tag) extends profile.api.Table[Testdatax1Row](_tableTag, Some("datasys_monitor"), "testDataX1") {
    /** Database column userName SqlType(VARCHAR), Length(20,true), Default(None) */
    val username: Rep[Option[String]] = column[Option[String]]("userName", O.Length(20, varying = true), O.Default(None))
    /** Database column age SqlType(DOUBLE), Default(None) */
    val age: Rep[Option[Double]] = column[Option[Double]]("age", O.Default(None))

    def * = (username, age) <> (Testdatax1Row.tupled, Testdatax1Row.unapply)
  }
}
