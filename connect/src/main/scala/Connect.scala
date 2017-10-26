import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import message.NewMsg
import service.{Server, Visitor}

import scala.concurrent.duration._

object Connect {
  def main(args: Array[String]): Unit = {
    startRemoteLookupSystem()
  }

  def startRemoteLookupSystem(): Unit = {
    //    val system = ActorSystem("client")
    //    val remotePath =
    //      "akka.tcp://ChatServer@127.0.0.1:9999/user/room"
    //    val actor = system.actorOf(Props(classOf[Visitor], remotePath), "visitor")
    //
    //    println("Started Client")
    //    var count = 0
    //    import system.dispatcher
    //    system.scheduler.schedule(5.second, 5.second) {
    //      count += 1
    //      actor ! NewMsg(s"hello $count")
    //    }
    println("Started Connect Server")
    val system = ActorSystem("connect")
    val actor = system.actorOf(Props(Server("localhost", 9999)))

  }
}
