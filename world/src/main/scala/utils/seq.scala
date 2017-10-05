package utils

object ID {
  private var seq: Long = 0

  def next(): Long = {
    seq += 1
    seq
  }

  def current(): Long = {
    seq
  }
}
