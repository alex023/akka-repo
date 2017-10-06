package actor

import actor.Army._
import akka.actor.FSM
import scala.concurrent.duration._
import message.ArmyProtocol.{ResourceOff, Start}
import utils.Seq

object Army {

  sealed trait ArmyState

  case object Idle extends ArmyState

  case object Depart extends ArmyState

  case object Gathering extends ArmyState

  case object Attacking extends ArmyState

  case object ComeBack extends ArmyState

  case class Place(x: Int, y: Int) {
    def -(that: Place): Double = {
      val disY = this.y - that.y
      val disX = this.x - that.x
      math.sqrt(disY * disY + disX * disX)
    }
  }

  case class ArmyData(Id: Long = Seq.next,
                      moveSpeed: Int = 10,
                      from: Place,
                      to: Place) {
    implicit def intToDouble(x: Int): Double = x.toDouble
  }

  def apply(x: Int, y: Int): Place = {
    new Place(x, y)
  }

}

class Army extends FSM[ArmyState, ArmyData] {
  startWith(Idle, ArmyData(from = Place(0, 0), to = Place(0, 0)))
  when(Idle) {
    case Event(Start(to), _) =>
      stateData.copy(to = to)
      goto(Depart)
  }
  when(Depart) {
    case Event(ComeBack, _) =>
      stateData.copy(to = stateData.from)
      goto(ComeBack)

  }
  when(Gathering) {
    case Event(ResourceOff, _) =>
      goto(ComeBack)
  }
  when(Attacking, stateTimeout = 1 second) {
    case Event(StateTimeout, _) =>
      goto(ComeBack)
  }
  when(ComeBack,
       stateTimeout =
         ((stateData.to - stateData.from) / stateData.moveSpeed) second) {
    case Event(StateTimeout, _) =>
      stop()
  }
  whenUnhandled {
    case Event(e, s) =>
      log.warning("received unhandled request {} in state {}/{}",
                  e,
                  stateName,
                  s)
      stay
  }
}
