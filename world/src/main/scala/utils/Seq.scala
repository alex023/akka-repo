package utils

object Seq {
  private var seq: Long = 0

  def next(): Long = {
    this.synchronized {
      seq += 1
      seq
    }
  }

  def current(): Long = {
    seq
  }
}
