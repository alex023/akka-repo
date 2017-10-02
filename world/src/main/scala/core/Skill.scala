package core

object Skill {
  //已经施加技能影响到的游戏单元索引
  private var skilledAgent = Map.empty[Int, Vector[Agent]]

  def load(): Unit = {
    //todo:
    //从数据库中加载
  }

  //向全局对象施展技能
  def addSkill(define: SkillDefine): Unit = {
    //所有操作在确定了技能对应的 buf 是否存在后执行
    val buf = Buff(define.id)
    if (buf isEmpty) return

    var agents = Vector.empty[Agent]
    define.agentIds.foreach { id =>
      agents = agents ++ Agent.getAgents(id)
    }
    skilledAgent = skilledAgent + (define.id -> agents)
    agents.foreach(_.addBuf(buf.get))
  }

  //向全局对象取消技能效果
  def removeSkill(define: SkillDefine): Unit = {
    //所有操作在确定了技能对应的 buf 是否存在后执行
    val buf = Buff(define.id)
    if (buf isEmpty) return

    var agents = Vector.empty[Agent]
    define.agentIds.foreach { id =>
      agents = agents ++ Agent.getAgents(id)
    }
    skilledAgent = skilledAgent - define.id
    agents.foreach(_.removeBuf(buf.get.id))
  }
}

/*
* 技能定义，该对象明确了技能对什么 Agent 添加 buf
* agentIds,是技能能够影响到的AgentId 列表
*/
case class SkillDefine(id: Int, agentIds: Vector[Int])