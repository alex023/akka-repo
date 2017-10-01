package message

import actor.Army.Place


object ArmyProtocol {
  trait UserInteraction
  trait ArmyInteraction

  case object Cancel extends UserInteraction
  case class Start(to:Place) extends UserInteraction

  case object ResourceOff extends ArmyInteraction
  case object Attacked extends ArmyInteraction
  case object Timeout extends ArmyInteraction

}