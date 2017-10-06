package utils

import org.scalatest.{FunSpec, FunSuite, MustMatchers, WordSpec}

import scala.util.Random

class SeqTest extends WordSpec with MustMatchers {
  "一个Seq应该满足以下测试" should {
    "第一次调用next 之前，current 必须为0" in {
      Seq.current() must be(0)
    }
    "每次调用 next，current必须只加 1 " in {
      val beforeCallNum = Seq.current()
      Seq.next()
      Seq.current() must be(beforeCallNum + 1)

    }
    "多次调用 next后， current应该随之增加" in {
      val beforeCallNum = Seq.current()
      val testNum = Random.nextInt(10000) + 1
      for (i <- 0 until testNum) Seq next ()
      Seq.current() must be(testNum + beforeCallNum)
    }
  }
  //class SeqTest extends FunSepc with MustMatchers {
//  describe("test Seq") {
//    it("seq id should increased by next") {
//      val testNum = Random.nextInt(10000) + 1
//      for (i <- 0 until testNum) Seq next ()
//      Seq.current() must be(testNum)
//    }
//  }

}
