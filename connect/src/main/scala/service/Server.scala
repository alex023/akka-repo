package service

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorSystem, Props}
import akka.io.{IO, Tcp}

class Server(host: String, port: Int) extends Actor {

  import akka.io.Tcp._
  import context.system

  IO(Tcp) ! Bind(self, new InetSocketAddress(host, port))
  def receive = {
    case b @ Bound(localAddress) =>
      context.parent ! b
    case CommandFailed(_: Bind) => context stop self
    case c @ Connected(remote, local) =>
      val connection = sender()
      val handler = context.actorOf(Props(Agent(connection)))
      connection ! Register(handler)
  }
}

object Server {
  def apply(host: String, port: Int): Server = new Server(host, port)
}
