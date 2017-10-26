package basedata

object Gem extends ListenerSupport {

  sealed abstract class Reason

  case object GemChangePay extends Reason

  case object GemChangeWildlife extends Reason

  case object GemChangeConsume extends Reason

  case object GemChangeSold extends Reason

//  type S = Gem
//  type E = EventGemChange
//  type L = GemListener

  case class EventGemChange(accountId: String, change: Int, reason: Reason)

  trait GemListener extends Listener

}

class Gem(implicit accountId: String) extends ListenerSupport {
  private var value: Int = _

  def add(change: Int, reason: Gem.Reason): Unit = {
    value += change
    onChange(change, reason)
  }

  def dec(change: Int, reason: Gem.Reason): Unit = {
    value -= change
    onChange(change, reason)

  }

  private def onChange(change: Int, reason: Gem.Reason): Unit = {
    val event = Gem.EventGemChange(accountId, change, reason)
  }
}
