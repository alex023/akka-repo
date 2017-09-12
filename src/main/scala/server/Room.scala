package server

import akka.actor.Actor

class Room extends Actor{
  def receive: Receive = {
    case _=> println( "not implement")
  }
}
