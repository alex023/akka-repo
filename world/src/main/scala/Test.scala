import java.util.Date

import net.pcsir.utils.ArrayUtils

import scala.Predef
import scala.util.Random

object Test {
  def error(message: String): Nothing =
    throw new IllegalArgumentException(message)

  def divide(x: Int, y: Int): Int = {
    if (y != 0) x / y
    else error("divided is zero")
  }

  def main(args: Array[String]): Unit = {
    Random.setSeed(new Date().getTime)

    val array = 1.to(10).toArray
    for (i <- 1 to 5) {
      val rndArray = ArrayUtils.unSort(array)

//    val rndArray = array.sortWith { (x, y) =>
//      val change = Random.nextBoolean()
//      println(x, y, change)
//      change
//    }
      println(rndArray.mkString(","))
    }
  }
}
