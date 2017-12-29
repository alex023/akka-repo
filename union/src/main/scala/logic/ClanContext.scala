package logic

import dao.Member
import define.ClanJoinType

class ClanContext(val id: String,
                  var name: String,
                  clanImg: String,
                  description: String,
                  joinType: ClanJoinType.Value) {
  val members: Map[String, Member] = Map.empty
}

object ClanContext {
  def apply(id: String,
            name: String,
            clanImg: String,
            description: String,
            joinType: ClanJoinType.Value): ClanContext =
    new ClanContext(id, name, clanImg, description, joinType)
}
