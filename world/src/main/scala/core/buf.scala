package core

object Buff {
  private val buffs = Map.empty[Int, Buff]

  def load(): Unit = {
    //todo:
    //从数据库中加载
  }

  def apply(buffId: Int): Option[Buff] = {
    if (buffs contains buffId) {
      Some(buffs(buffId))
    } else {
      None
    }
  }
}

//游戏效果的属性定义，根据具体游戏确定该对象的属性
case class Buff(id: Int, health: Float)
