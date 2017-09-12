package server

import java.util.concurrent.TimeUnit

import akka.actor.{ActorSystem, Props}

object Main {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("ReactiveEnterprise")
    val roomRef = system.actorOf(Props[Room], "room")
    roomRef ! "ok"

    //    system.wait(1000L)
    TimeUnit.SECONDS.sleep(10)
    system.terminate()
  }
}
