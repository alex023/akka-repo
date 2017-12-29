package dao

import java.sql.Date

import define.ClanJoinType
import slick.jdbc.MySQLProfile.api._
import define.ClanJoinType.ClanJoin

object ClanBase {

  // Case class representing a row in our table:
  final case class ClanBase(id: Long = 0,
                            name: String,
                            clanImg: String,
                            description: String,
                            joinType: Int,
                            memberCap: Int,
                            memberLen: Int,
                            ownerId: String,
                            medal: Int,
                            createTime: Date)

  // Schema for the "ClanBase" table:
  final class ClanBaseTable(tag: Tag) extends Table[ClanBase](tag, "clan") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def name = column[String]("name")

    def clanImg = column[String]("clanimg")

    def description = column[String]("description")

    def joinType = column[Int]("join_type")

    def memberCap = column[Int]("member_cap")

    def memberLen = column[Int]("member_len")

    def ownerId = column[String]("owner_id")

    def medal = column[Int]("medal")

    def createTime = column[Date]("create_time")

    def * =
      (id,
       name,
       clanImg,
       description,
       joinType,
       memberCap,
       memberLen,
       ownerId,
       medal,
       createTime).mapTo[ClanBase]

    implicit def ClanJoinToInt(clanJoinType: ClanJoin): Int = clanJoinType.id

    implicit def IntToClanJoin(id: Int): ClanJoin = ClanJoinType.apply(id)

  }

}
