package actor

import akka.actor.{Actor, ActorRef}

class Clan(id: String) extends Actor {
  val onlineMember: Map[String, ActorRef] = Map.empty

  override def preStart(): Unit = ???

  override def receive = ???
}
