package core

import scala.collection.mutable

//全局的 Agent 容器
object Agent {
  //所有游戏对象的全局索引，Gid->Agent
  private var agents = new mutable.HashMap[Long, Agent]()

  def load(): Unit = {
    //todo:
    //从数据库中加载
  }

  def getAgent(agentId: Long): Option[Agent] =
    if (agents contains agentId) {
      Some(agents(agentId))
    } else None

  def getAgents(tId: Long): Vector[Agent] = {
    agents.filter { case (_, agent) => agent.tid == tId }.values.toVector
  }

  def removeAgent(agentId: Long): Unit = {
    agents -= agentId
  }

  def addAgent(agent: Agent): Unit = {
    agents += (agent.id -> agent)
  }
}

/*
 * 游戏单位实现 Buf 效果的单元基础定义，任何需要实现 Buf 效果的子类，都需要继承该类。
 * 在实现的时候，只需要在重载固定函数的时候，修改自身属性即可.
 * Note:Agent不支持多线程，在应用时需要确保同一时刻只会被一个方法驱动
 */
abstract class Agent {
  val id: Long
  val tid: Int
  final private val buffs = new mutable.HashMap[Int, Buff]()

  final def addBuf(buf: Buff): Unit = {
    buffs += (buf.id -> buf)
    onAddBuf(buf)
  }

  final def removeBuf(bufId: Int): Unit = {
    if (buffs contains bufId) {
      val buf = buffs(bufId)
      buffs -= bufId
      onRemoveBuf(buf)
    }
  }

  final def currentBuf(): Vector[Buff] = {
    buffs.values.toVector
  }

  protected def onAddBuf(buf: Buff)

  protected def onRemoveBuf(buf: Buff)
}
