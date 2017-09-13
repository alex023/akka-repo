package service

import akka.actor.{Actor, ActorRef, Terminated}
import akka.io.Tcp.Connected
import message.{Connect, NewMsg}

import scala.collection.mutable

class Room extends Actor {
  val onlineMember = new mutable.HashMap[ActorRef, Boolean]()
  var count = 0

  def receive: Receive = {
    case Connect =>
      onlineMember.put(context.sender(), true)
      context.watch(context.sender())
      context.sender() ! Connected
    case ms: NewMsg =>
      //接收到消息，并分发给在线玩家
      println("receive content:", ms.content)
      count += 1
      for (actorRef <- onlineMember.keys) {
        actorRef ! ms
      }
    case t:Terminated =>
      onlineMember.remove(t.actor)
      println(s"remote ${context.sender()} is removed! currentOnlinemeber ${onlineMember.size}")
      println(onlineMember)
    case "getOnlineVisitor" =>
      println(s"当前在线数 ${onlineMember.size}")
      sender() ! onlineMember.size
    case "getMsgNum" =>
      sender() ! count
    case msg:Any =>
      println(s"receive unkown type $msg")
//      throw new ClassNotFoundException
  }

}
