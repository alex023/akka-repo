package actor

import akka.actor.{Actor, Props}
import akka.actor.SupervisorStrategy.{Escalate, Restart, Resume, Stop}

class Supervisor extends Actor {

  import akka.actor.OneForOneStrategy
  import scala.concurrent.duration._

  override val supervisorStrategy =
    OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 minute) {
      case _: ArithmeticException      => Resume
      case _: NullPointerException     => Restart
      case _: IllegalArgumentException => Stop
      case _: Exception                => Escalate
    }

  override def receive: Receive = {
    case p: Props => sender() ! context.actorOf(p)
  }
}
