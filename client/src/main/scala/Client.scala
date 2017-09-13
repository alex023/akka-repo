import akka.actor.{ActorSystem, Props}

object Client {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("client")
    val userRef = system.actorOf(Props[service_client.Visitor], "visitor")


    userRef ! "test"
    userRef ! new message.NewMsg("messag new")
    println("client to do")

  }
}
