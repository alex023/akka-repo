package service

import akka.actor.{Actor, ActorRef, Terminated}
import message.{ChatMessage, Connect, Request, Response}

import scala.collection.mutable
import scala.collection.parallel.immutable

class Room extends Actor {
  val onlineMember = new mutable.HashMap[ActorRef, Boolean]()

  def receive: Receive = {
    case Connect =>
      onlineMember.put(context.sender(), true)
      context.watch(context.sender())
    case Request(content) =>
      //接收到消息，并分发给在线玩家
      println("receive content:", content)
      val broadMsg = new Request(content)
      for (actorRef <- onlineMember.keys) {
        actorRef ! broadMsg
      }
    case Terminated =>
      onlineMember.remove(context.sender())
      println(s"remote ${context.sender()} is removed!")
    case _ =>
      println("not implement")
  }

}
