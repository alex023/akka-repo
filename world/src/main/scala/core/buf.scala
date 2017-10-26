package core

object Buff {
  private var buffs = Map.empty[Int, Buff]

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

  def apply(id: Int,
            lvl: Int,
            cooldown: Int,
            speed: Double,
            maxHP: Double,
            produceRate: Double,
            lostFactor: Double,
            durationTime: Int): Buff = {
    val buff = Buff(id,
                    lvl,
                    cooldown,
                    speed,
                    maxHP,
                    produceRate,
                    lostFactor,
                    durationTime)
    buffs += (id -> buff)
    buff
  }
}

//游戏效果的属性定义，根据具体游戏确定该对象的属性进行调整，必须覆盖所有游戏单元的信息
case class Buff private (id: Int,
                         lvl: Int,
                         cooldown: Int,
                         speed: Float,
                         maxHP: Float,
                         produceRate: Float,
                         lostFactor: Float,
                         durationTime: Int)
