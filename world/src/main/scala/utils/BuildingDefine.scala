package utils

case class BuildingDefine private (tid: Int, Outer_W: Int, Outer_H: Int)

object BuildingDefine {
  private val buildingDefines: Map[Int, BuildingDefine] = mock

  //模拟初始化代码，用于测试
  private def mock = {
    var tmpBuildingDefines = Map.empty[Int, BuildingDefine]
    for (i <- 1 to 10) {
      val bd = BuildingDefine(i, i % 5, i % 5)
      tmpBuildingDefines += (i -> bd)
    }
    tmpBuildingDefines
  }

  def contains(tid: Int): Boolean = {
    buildingDefines contains tid
  }

  def apply(tid: Int): BuildingDefine =
    buildingDefines(tid)

}
