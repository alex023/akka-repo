package utils

import org.scalatest.FunSuite

import scala.util.Random

class SeqTest extends FunSuite {
  test("testNext") {
    Seq.next()
  }

  test("testCurrent") {
    val testNum = Random.nextInt(10000) + 1
    for (i <- 0 until testNum) Seq next ()
    assert(Seq.current() == testNum)
  }

}
