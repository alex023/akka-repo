package core

//全局的 Agent 容器
object Agent {
  //所有游戏对象的全局索引，Gid->Agent
  private var agents = Map.empty[Int, Agent]

  def getAgent(agentId: Int): Option[Agent] = if (agents contains agentId) {
    Some(agents(agentId))
  } else None

  def getAgents(tId: Int): Vector[Agent] = {
    agents.filter { case (key, agent) => agent.tid == tId }.values.toVector
  }

  def removeAgent(agentId: Int): Unit = {
    agents = agents.drop(agentId)
  }

  def addAgent(agent: Agent): Unit = {
    agents = agents + (agent.id -> agent)
  }
}

/*
游戏单位实现 Buf 效果的单元基础定义，任何需要实现 Buf 效果的子类，都需要继承该类。
在实现的时候，只需要在重载固定函数的时候，修改自身属性即可
 */
trait Agent {
  val id: Int
  val tid: Int
  private var buffs = Map.empty[Int, Buf]

  final def addBuf(buf: Buf): Unit = {
    buffs = buffs + (buf.id -> buf)
    onAddBuf(buf)
  }

  final def removeBuf(bufId: Int): Unit = {
    if (buffs contains bufId) {
      buffs = buffs.drop(bufId)
      onRemoveBuf(bufId)
    }
  }

  protected def onAddBuf(buf: Buf)

  protected def onRemoveBuf(id: Int)
}
