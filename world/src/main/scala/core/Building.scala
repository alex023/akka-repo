package core

class Building(id: Long, tid: Int) extends Agent(id, tid) {
  var maxHP: Float = _
  var currHP: Float = _

  override protected def onAddBuf(buf: Buff): Unit = {
    currHP += maxHP * buf.maxHP
  }

  override protected def onRemoveBuf(buf: Buff): Unit = {
    currHP -= maxHP * buf.maxHP
  }

}
