package repository

import java.sql.Date

import com.typesafe.config.ConfigFactory
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration._

case class Pay(transactionid: String,
               uidClient: String,
               channel: Int,
               accountId: String,
               receipt: String,
               receiptcipheredPayload: String,
               receiptDigest: String,
               response: String,
               responseTransactionId: String,
               responsePurchaseTime: Date,
               responseProductId: String,
               responseVerifyState: Boolean = false,
               responsed: Boolean = false)

class Pays(tag: Tag) extends Table[Pay](tag, "record") {

  def transactionId = column[String]("transaction_id", O.PrimaryKey)

  def uidClient = column[String]("uid_client")

  def channel = column[Int]("channel")

  def accountId = column[String]("account_id")

  def receipt = column[String]("receipt")

  def receiptcipheredPayload = column[String]("receipt_data")

  def receiptDigest = column[String]("receipt_digest")

  def response = column[String]("response")

  def responseTransactionId = column[String]("resp_transaction_id")

  def responsePurchaseTime = column[Date]("resp_purchase_time")

  def responseProductId = column[String]("resp_product_id")

  def responseVerifyState = column[Boolean]("resp_verify_state")

  def responsed = column[Boolean]("responsed")
  override def * =
    (transactionId,
     uidClient,
     channel,
     accountId,
     receipt,
     receiptcipheredPayload,
     receiptDigest,
     response,
     responseTransactionId,
     responsePurchaseTime,
     responseProductId,
     responseVerifyState,
     responsed).mapTo[Pay]
}

object Test extends App {

  import scala.concurrent.ExecutionContext.Implicits.global
  lazy val pays = TableQuery[Pays]

  val db = Database.forConfig("akka.mysqldb")

  def exec[T](program: DBIO[T]): T = Await.result(db.run(program), 1.second)
  //表初始化，暂时不执行
  val setup = DBIO.seq(pays.schema.create)
  val setupFuture = db.run(setup)

  val xx = for (item <- pays
                if item.accountId === "FFE58BF2-307C-11E7-A346-408D5CB790C2")
    yield item
  println(xx.result.statements.mkString)

  try {
//    val future = db.run(
//      pays
//        .filter(_.accountId === "FFE58BF2-307C-11E7-A346-408D5CB790C2")
//        .result)
//    future.onComplete {
//      case Success(value) =>
//        value
//          .map { x =>
//            println(s" ${x.accountId} ${x.channel} ${x.receiptDigest}")
//          }
//      case Failure(e) => println(e)
//    }
    val action = pays
      .filter(item => item.accountId === "FFE58BF2-307C-11E7-A346-408D5CB790C2")
      .result
    exec(action).foreach(
      x =>
        println(
          s" ${x.accountId} ${x.channel} ${x.receiptDigest} ${x.responsePurchaseTime.getDate}")
    )
  } catch {
    case e: Exception => println(e)
  } finally {
    db.close()
  }

}
