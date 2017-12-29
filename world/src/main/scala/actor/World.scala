package actor

import akka.actor.{Actor, ActorRef}
import message.{Offline, Online}

class World extends Actor {
  override def receive: Receive = {
    case _ => println("World receive message")
  }
}
