package core

import org.scalatest.{BeforeAndAfterAll, MustMatchers, WordSpec}

class AgentTest extends WordSpec with BeforeAndAfterAll with MustMatchers {

  class Building(override val id: Long,
                 override val tid: Int,
                 var health: Float)
      extends Agent(id, tid) {

    override protected def onAddBuf(buf: Buff): Unit = {
      health += buf.maxHP
    }

    override protected def onRemoveBuf(buf: Buff): Unit = {
      health -= buf.maxHP
    }
  }

  var building: Building = null
  var buf: Buff = null

  override def beforeAll() {
    info("beforeAll")
    building = new Building(123, 1, 100)
    buf = Buff(123, 1, 2, 15.0, 10.0, 5.0, 0.0, 0)
  }

  override def afterAll() {
    info("afterAll")
  }

  "AgentTest" must {
    "addBuf" in {
      building.addBuf(buf)
      building.currentBuf().size must be(1)
    }

    "removeBuf" in {
      building.removeBuf(buf.id)
      building.currentBuf().size must be(0)

    }

    "currentBuf" in {
      building.addBuf(buf)
      building.addBuf(buf)
      building.currentBuf().size must be(1)
      building.currentBuf().foreach { buf =>
        println(s"$buf")
      }
    }

  }
}
