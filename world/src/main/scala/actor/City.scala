package actor

import akka.actor.{Actor, ActorRef}

class City(parent:ActorRef) extends Actor {
  override def preStart(): Unit = {
    //todo:订阅地图某个点的事件，或许交由 player
  }
  override def receive: Receive = {
    case _ =>
      println("TODO")
  }
}
