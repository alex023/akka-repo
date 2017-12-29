package define

object ClanJoinType extends Enumeration {
  type ClanJoin = Value
  val ClanJoinNoApproval = Value(0)
  val ClanJoinNeedApproval = Value(1)
  val ClanJoinDecline = Value(2)

}
