package service

import Define.DependService
import akka.actor.{Actor, ActorIdentity, ActorRef, Identify, ReceiveTimeout, Terminated}
import message.Connect

import concurrent.duration._

class Agent() extends Actor {
  private val  path = DependService.path

  sendIdentifyRequest()

  def sendIdentifyRequest(): Unit = {
    context.actorSelection(path) ! Identify(path)
    import context.dispatcher
    context.system.scheduler.scheduleOnce(3.seconds, self, ReceiveTimeout)

    context.actorSelection(path) tell(Connect, self)

  }

  def receive = identifying

  def identifying: Actor.Receive = {
    case ActorIdentity(`DependService`, Some(actor)) =>
      context.watch(actor)
      context.become(active(actor))
    case ActorIdentity(`path`, None) => println(s"Remote actor not available: $path")
    case ReceiveTimeout => sendIdentifyRequest()
    case _ => println("Not ready yet")
  }

  def active(actor: ActorRef): Actor.Receive = {
    case Terminated(`actor`)=>
      context.become(identifying)
    case _ =>
    //do nothing
  }
}
