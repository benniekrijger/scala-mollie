package nl.mollie

import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import akka.event.LoggingAdapter
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.{StatusCodes, _}
import akka.stream.ActorMaterializer
import akka.testkit.{ImplicitSender, TestKit}
import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import nl.mollie.commands.CreatePaymentIdeal
import nl.mollie.config.MollieConfig
import nl.mollie.connection.HttpServer
import nl.mollie.responses.{MollieFailure, PaymentResponse}
import org.json4s.native.JsonMethods._
import org.json4s.{DefaultFormats, Formats, Serialization, jackson, _}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.concurrent.duration.FiniteDuration

class MollieCommandActorSpec(_system: ActorSystem) extends TestKit(_system) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll with Json4sSupport with MollieFactory {
  implicit val formats: Formats = DefaultFormats
  implicit val dispatcher: ExecutionContextExecutor = system.dispatcher
  implicit val jacksonSerialization: Serialization = jackson.Serialization
  implicit val materializer = ActorMaterializer()
  val timeoutDuration = FiniteDuration(3, TimeUnit.SECONDS)

  def this() = this(ActorSystem("MollieCommandActorSpec"))

  override def afterAll() {
    TestKit.shutdownActorSystem(system)
  }

  val config: MollieConfig = mollieConfig()
  val log: LoggingAdapter = _system.log

  "A MollieCommandActor" must {

    "be able to create an ideal payment" in {
      val testConnection: HttpServer = new HttpServer {
        override def sendRequest(request: HttpRequest): Future[HttpResponse] = {
          request.entity
            .toStrict(timeoutDuration)
            .map(_.data)
            .map(_.utf8String)
            .flatMap { body =>
              val expectedBody =
                """
                  |{
                  |   "issuer": "ideal_TESTNL99",
                  |   "amount": 10.0,
                  |   "description": "",
                  |   "redirectUrl": "http://redirect",
                  |   "webhookUrl": "http://webhook",
                  |   "locale": "nl",
                  |   "metadata":{
                  |     "id":"some id"
                  |   },
                  |   "method": "ideal"
                  |}
                """.stripMargin.replaceAll("""\s+""", "")

              if (body.stripMargin.replaceAll("""\s+""", "") == expectedBody) {
                val responseJson = parse(
                  """
                      {
                        "id":              "tr_7UhSN1zuXS",
                        "mode":            "test",
                        "createdDatetime": "2016-07-28T17:11:13.0Z",
                        "status":          "open",
                        "expiryPeriod":    "PT15M",
                        "amount":          10.00,
                        "description":     "My first payment",
                        "metadata": {
                            "order_id": "12345"
                        },
                        "locale": "nl",
                        "profileId": "pfl_QkEhN94Ba",
                        "links": {
                            "paymentUrl":  "https://www.mollie.com/payscreen/select-method/7UhSN1zuXS",
                            "redirectUrl": "https://webshop.example.org/order/12345/"
                        }
                      }
                    """
                )

                Marshal(responseJson)
                  .to[MessageEntity]
                  .map { entity =>
                    HttpResponse(
                      status = StatusCodes.Created,
                      entity = entity
                    )
                  }
              } else {
                log.warning("body={} != expected={}", body, expectedBody)
                Future.successful(
                  HttpResponse(status = StatusCodes.BadRequest)
                )
              }
            }
          }
      }

      val commandActor = system.actorOf(
        MollieCommandActor.props(
          connection = testConnection,
          config = config
        )
      )


      commandActor ! CreatePaymentIdeal(
        amount = 10,
        description = "",
        issuer = Some("ideal_TESTNL99"),
        redirectUrl = "http://redirect",
        webhookUrl = Some("http://webhook"),
        locale = Some("nl"),
        metadata = Map(
          "id" -> "some id"
        )
      )

      expectMsgPF(timeoutDuration) {
        case _: PaymentResponse => true
      }
    }

    "respond failure when unable to create ideal payment" in {
      val testConnection: HttpServer = new HttpServer {
        override def sendRequest(request: HttpRequest): Future[HttpResponse] = Future.failed(new RuntimeException("Timeout"))
      }

      val commandActor = system.actorOf(
        MollieCommandActor.props(
          connection = testConnection,
          config = config
        )
      )


      commandActor ! CreatePaymentIdeal(
        amount = 10,
        description = "",
        issuer = Some("ideal_TESTNL99"),
        redirectUrl = "http://redirect",
        webhookUrl = Some("http://webhook"),
        locale = Some("nl"),
        metadata = Map(
          "id" -> "some id"
        )
      )

      expectMsgPF(timeoutDuration) {
        case _: MollieFailure => true
      }
    }
  }

}
