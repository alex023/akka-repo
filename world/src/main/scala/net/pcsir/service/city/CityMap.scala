package net.pcsir.service.city

import scala.language.postfixOps

case class Point(x: Int, y: Int) {
  def isOutOfBoundary: Boolean = {
    if (x < 0 || x > CityMap.BOUNDARY_W || y < 0 || y > CityMap.BOUNDARY_H) {
      false
    } else {
      true
    }
  }
}

class Building(val id: Int, val tid: Int, var x: Int, var y: Int) {
  b =>
  def updatePoint(x: Int, y: Int): Building = {
    new Building(b.id, b.tid, x, y)
  }
}

class CityMap(var buildings: Map[Int, Building] = Map.empty) {

  class Surface {
    private val surface =
      Array.ofDim[Option[Int]](CityMap.BOUNDARY_W, CityMap.BOUNDARY_H)

    def isExist(buildId: Int, point: Point, define: BuildingDefine): Boolean = {
      var id: Option[Int] = None
      for (y <- point.y until define.Outer_H + point.y) {
        for (x <- point.x until define.Outer_W + point.x) {
          id = surface(x)(y)
          if (id.isEmpty || id.get != buildId) return false
        }
      }
      true
    }

    def isClean(point: Point, define: BuildingDefine): Boolean = {
      for (y <- point.y until define.Outer_H + point.y) {
        for (x <- point.x until define.Outer_W + point.x) {
          if (surface(x)(y).nonEmpty) return false
        }
      }
      true
    }

    def isCleanExcept(buildId: Int,
                      point: Point,
                      define: BuildingDefine): Boolean = {
      var id: Option[Int] = None
      for (y <- point.y until define.Outer_H + point.y) {
        for (x <- point.x until define.Outer_W + point.x) {
          id = surface(x)(y)
          if (id.nonEmpty && id.get != buildId) return false
        }
      }
      true
    }

    def addBuilding(building: Building, define: BuildingDefine): Unit = {
      for (y <- building.y until define.Outer_H + building.y;
           x <- building.x until define.Outer_W + building.x) {
        surface(x)(y) = Some(building.id)
      }
    }

    def getBuildingId(x: Int, y: Int): Option[Int] = {
      surface(x)(y)
    }

    def clean(point: Point, define: BuildingDefine): Unit = {
      for (y <- point.y until define.Outer_H + point.y;
           x <- point.x until define.Outer_W + point.x) {
        surface(x)(y) = None
      }
    }
  }

  private val surface = {
    val tmpSurface = new Surface()
    buildings.foreach {
      case (id, building) =>
        if (BuildingDefine contains id) {
          val define = BuildingDefine(id)
          tmpSurface.addBuilding(building, define)
        } else {
          println(s"surface can not found define $id")
        }
    }
    tmpSurface
  }

  def getBuilding(gid: Int): Building = {
    buildings(gid)
  }

  def getBuilding(x: Int, y: Int): Option[Building] = {
    val id = surface.getBuildingId(x, y)
    if (id.isEmpty) None
    else {
      Some(buildings(id.get))
    }
  }

  def getBuildings(tid: Int): Set[Building] = {
    buildings
      .filter {
        case (_, building) => building.tid == tid
      }
      .values
      .toSet
  }

  def addBuilding(newBuild: Building): Boolean = {
    val place = Point(x = newBuild.x, y = newBuild.y)
    if (place.isOutOfBoundary || !BuildingDefine.contains(newBuild.tid))
      return false

    val define = BuildingDefine(newBuild.tid)
    if (!isExist(newBuild.id, place, define)) false
    else {
      innerAddingBuilding(newBuild, define)
      true
    }
  }

  def addBuilding(newBuilds: Array[Building]): Boolean = {
    newBuilds.foreach(building =>
      if ((buildings contains building.id) || !(BuildingDefine contains building.tid)) {
        return false
      } else {
        val place = Point(building.x, building.y)
        val define = BuildingDefine(building.tid)
        if (!surface.isClean(place, define)) {
          return false
        }
    })
    newBuilds.foreach(building =>
      innerAddingBuilding(building, BuildingDefine(building.tid)))
    true
  }

  def moveBuilding(building: Building, from: Point, to: Point): Boolean = {
    if (!BuildingDefine.contains(building.tid)) {
      return false
    }
    val define = BuildingDefine(building.tid)
    if (!isExist(building.id, from, define) ||
        surface.isExist(building.id, from, define) ||
        surface.isCleanExcept(building.id, to, define))
      false
    else {
      surface.clean(from, define)
      surface.addBuilding(building, define)
      true
    }
  }

  def exchangeBuilding(a: Building, b: Building): Boolean = {
    if (!buildings.contains(a.id) || !buildings.contains(b.id)) {
      false
    } else if (!(BuildingDefine contains a.tid) || !(BuildingDefine contains b.tid)) {
      false
    } else {
      val aPoint = Point(a.x, a.y)
      val aBt = BuildingDefine(a.tid)
      val bPoint = Point(b.x, b.y)
      val bBt = BuildingDefine(b.tid)

      if (canExchange(a, b, aBt, bBt)) {
        surface.clean(aPoint, aBt)
        surface.clean(bPoint, bBt)
        val newA = a.updatePoint(b.x, b.y)
        val newB = b.updatePoint(a.x, a.y)
        innerAddingBuilding(newA, aBt)
        innerAddingBuilding(newB, bBt)
        true
      } else {
        false
      }
    }
  }

  private def canExchange(a: Building,
                          b: Building,
                          btA: BuildingDefine,
                          btB: BuildingDefine): Boolean = {
    var result = true
    if (btA.tid == btB.tid) {
      //result = true
    } else {
      // 判定交换后的区域是否能够放置是否越界
      result = isClearIgnoreSameBuild(a, btB) /*判断b能否在a区域放置*/ && isClearIgnoreSameBuild(
        b,
        btA)
    }
    result
  }

  // isClearIgnoreSameBuild 判断目标地范围内，是否为空或者为同一建筑
  private def isClearIgnoreSameBuild(target: Building,
                                     bt: BuildingDefine): Boolean = {
    var result = true
    // 判定是否越界
    if (isOutBoundary(target.x,
                      target.y,
                      target.x + bt.Outer_W,
                      target.y + bt.Outer_H)) {
      result = false
    }
    var pointValue: Option[Int] = None
    // 判定能否放置
    for (y <- 0 until bt.Outer_H; x <- 0 until bt.Outer_W) {
      pointValue = surface.getBuildingId(target.x + x + x, target.y + y)
      if (pointValue.nonEmpty && pointValue.get != target.id) {
        result = false
      }
    }
    result
  }

  private def isOutBoundary(minX: Int,
                            minY: Int,
                            maxX: Int,
                            maxY: Int): Boolean = {
    if (minX >= maxX || minY >= maxY || minX < 0 || maxX > CityMap.BOUNDARY_W || minY < 0 || maxY > CityMap.BOUNDARY_H) {
      true
    } else {
      false
    }
  }

  private def innerAddingBuilding(newB: Building,
                                  define: BuildingDefine): Unit = {
    buildings += (newB.id -> newB)
    surface.addBuilding(newB, define)
  }

  //判断地图上，对应范围内是否已经存在该建筑
  def isExist(id: Int, point: Point, define: BuildingDefine): Boolean = {
    if (buildings.contains(id) || surface.isClean(point, define)) true
    else false
  }
}

object CityMap {
  // 地图的范围大小为[0，44)的正方形
  val BOUNDARY_W: Int = 44
  val BOUNDARY_H: Int = 44

  def apply(buildings: Map[Int, Building] = Map.empty): CityMap =
    new CityMap(buildings)
}
