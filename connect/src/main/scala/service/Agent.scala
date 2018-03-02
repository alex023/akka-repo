package service

import define.DependService
import akka.actor.{
  Actor,
  ActorIdentity,
  ActorRef,
  Identify,
  PoisonPill,
  ReceiveTimeout,
  Terminated
}
import akka.io.Tcp.{PeerClosed, Received}

import scala.concurrent.duration._

class Agent(connect: ActorRef) extends Actor {
  private val path = DependService.path
  //todo:暂时忽略，正式使用时，不可注释
  sendIdentifyRequest()

  //当没有backend连接的时候，发起请求，以确保有后端服务的支撑
  def sendIdentifyRequest(): Unit = {
    //请求业务服务对象
    context.actorSelection(path) ! Identify(path)

    import context.dispatcher
    context.system.scheduler.scheduleOnce(3.seconds, self, ReceiveTimeout)
  }

  //todo:实现断线的相关操作
  override def postStop(): Unit = {
    println("Agent is closed")
  }

  def receive = identifying

  override def preStart(): Unit = super.preStart()

  //todo:在三秒钟内，实现客户端请求的验证和游戏服的对象绑定
  def identifying: Actor.Receive = {
    case ActorIdentity(`DependService`, Some(actor)) =>
      context.watch(actor)
      actor ! message.Connect
      context.become(active(actor))
    case ActorIdentity(`path`, None) =>
      println(s"Remote actor not available: $path")
    case ReceiveTimeout =>
      sendIdentifyRequest()
      context.self ! PoisonPill
    case Received(msg) =>
      println(s"msg=${msg.decodeString("UTF-8")}")
    case other => println(s"received $other Not ready yet")
  }

  //客户端及服务后端正常情况下，执行消息转发
  def active(actor: ActorRef): Actor.Receive = {
    case ReceiveTimeout =>
    //do nothing
    case Terminated(`actor`) =>
      context.become(identifying)
    case PeerClosed =>
      context.self ! PoisonPill
    case Received(msg) =>
      //将客户端TCP发送的消息转发给backend
      actor.forward()
      actor ! msg
      actor forward ()
    case pushmsg =>
      //将backend消息，推送给客户端
      connect ! pushmsg
  }

}

object Agent {
  def apply(connect: ActorRef): Agent = new Agent(connect)

}
