package net.pcsir.service.world

import net.pcsir.service.module.Define.{GridLevel, ResourceLevel}
import net.pcsir.service.module.{Define, Point, Resource}

import scala.util.Random

/**
  * 地区，包含多个网格的正方形区域，介于Ground与网格Point之间，决定该地生成的资源等级。越靠近Ground中心，资源等级越高
  * @param topCorner  左上角的坐标点
  */
class Area(val topCorner: Point) {
  //区块的资源起始等级
  private val startLvl: GridLevel = topCorner.x / Area.WIDTH + 1
  private val randMax = Area.RAND_RESOURCE_RANGE.max
  private val bottomCorner: Point =
    Point(topCorner.x + Area.WIDTH, topCorner.y + Area.HEIGHT)

  /**
    * 根据区域的所处位置确定的初始资源等级，随机生成资源
    * @return
    */
  def createSource(): Resource = {
    var newResource = Resource.noResource
    val level = randLevel()
    val points = randPoints()
    //将资源加入到Ground中
    var ok = false
    for (rndPoint <- points if !ok) {
      newResource = Resource(rndPoint, level)
      ok = Ground.addFacility(newResource)
    }

    newResource
  }

  //获取指定区域内控制地貌的随机顺序列表
  private def randPoints(): Array[Point] = {
    import net.pcsir.utils.ArrayUtils
    val vacantPoints =
      Ground
        .vacantPoint(topCorner, bottomCorner, Define.TypeResource)
    ArrayUtils.unSort(vacantPoints)
  }
  private def randLevel(): ResourceLevel = {
    val rndResourceLvl = Random.nextInt(randMax)
    val step = (Area.RAND_RESOURCE_RANGE sliding 2)
      .indexWhere(item => rndResourceLvl >= item(0) && rndResourceLvl < item(1))

    assert(step == -1)

    startLvl + step
  }
}

object Area {

  /**
    * 地区的固定宽度
    */
  val WIDTH = 64

  /**
    * 地区的固定高度
    */
  val HEIGHT = 64
  //随机资源的范围
  val RAND_RESOURCE_RANGE = Array(0, 55, 80, 95, 100) //.sortWith((x, y) => x < y)

  private def apply(point: Point): Area = new Area(point)

  /**
    * 生成遍布Ground地图的Grid区块,ground的W与H，必须能够被Width、Height整除
    * @param groundW  地图的宽
    * @param groundH  地图的高
    * @return 以二维数组呈现的区块
    */
  def apply(groundW: Int, groundH: Int): Array[Array[Area]] = {
    val widths = groundW / WIDTH
    val heights = groundH / HEIGHT
    val result = Array.fill(widths, heights)(Area(Point(0, 0)))
    for { y <- 0 until heights; x <- 0 until widths } {
      val point = Point(x * WIDTH, y * HEIGHT)
      result(x)(y) = Area(point)
    }
    result
  }

}
