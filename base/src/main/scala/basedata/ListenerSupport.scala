package basedata

import scala.collection.mutable.ArrayBuffer

trait ListenerSupport {
  type S <: Source
  type L <: Listener
  type E <: Event

  trait Event {
    var source: S = _
  }

  trait Listener {
    def occurred(e: E): Unit
  }

  trait Source {
    this: S =>
    private val listeners = new ArrayBuffer[L]

    def add(l: L) {
      listeners += l
    }

    def remove(l: L) {
      listeners -= l
    }

    def fire(e: E): Unit = {
      e.source = this
      for (l <- listeners) l.occurred(e)
    }
  }

}
