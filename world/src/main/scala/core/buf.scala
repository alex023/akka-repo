package core

object Buf{
  private var buffs=Map.empty[Int,Buf]
  def apply(buffId:Int):Option[Buf]={
    if (buffs contains buffId){
      buffs(buffId)
    }else{
      None
    }
  }
}
//游戏效果的属性定义，根据具体游戏确定该对象的属性
case class Buf(id: Int, health: Float)
