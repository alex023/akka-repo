package utils

object Seq {
  @volatile
  private var seq: Long = 0

  def next(): Long = {
    seq += 1
    seq
  }

  def current(): Long = {
    seq
  }
}
