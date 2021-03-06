package service


import akka.actor.{Actor, ActorIdentity, ActorRef, Identify, ReceiveTimeout, Terminated}
import message.{BroadMsg, Connect, Connected, NewMsg}

import concurrent.duration._

class Visitor(path: String) extends Actor {
  private var sendCount = 0
  private var receiveCount=0
  val user = "visitor"

  sendIdentifyRequest()

  def sendIdentifyRequest(): Unit = {
    context.actorSelection(path) ! Identify(path)

    import context.dispatcher
    context.system.scheduler.scheduleOnce(3.seconds, self, ReceiveTimeout)
  }

  def receive = identifying

  def identifying: Actor.Receive = {
    case ActorIdentity(`path`, Some(actor)) =>
      context.watch(actor)
      context.actorSelection(path) tell(Connect, self)
      context.become(active(actor))
    case ActorIdentity(`path`, None) => println(s"Remote actor not available: $path")
    case ReceiveTimeout => sendIdentifyRequest()
    case _ => println("Not ready yet")
  }


  def active(actor: ActorRef): Actor.Receive = {
    case Connected => println(s"$user connected")
    case msg: NewMsg =>
      sendCount += 1
      println(s"$user push msg:$msg.content")
      actor ! msg
    case BroadMsg(content) =>
      receiveCount +=1
      println(s"$user receive msg: $content")
    case "getSendNum" =>
      sender ! sendCount
    case "getReceiveNum"=>
      sender ! receiveCount
    case Terminated(`actor`) =>
      println("Calculator terminated")
      sendIdentifyRequest()
      context.become(identifying)
    case ReceiveTimeout =>
    // ignore
  }
}