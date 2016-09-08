package nl.mollie

import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.{StatusCodes, _}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import akka.testkit.{ImplicitSender, TestKit}
import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import nl.mollie.commands.CreatePaymentBanktransfer
import nl.mollie.config.MollieConfig
import nl.mollie.connection.HttpServer
import nl.mollie.models.PaymentLocale
import nl.mollie.responses.PaymentResponse
import org.json4s.native.JsonMethods._
import org.json4s.{DefaultFormats, Formats, Serialization, jackson, _}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration

class MollieCommandActorSpec(_system: ActorSystem) extends TestKit(_system) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll with Json4sSupport with MollieFactory {
  implicit val formats: Formats = DefaultFormats
  implicit val dispatcher = system.dispatcher
  implicit val jacksonSerialization: Serialization = jackson.Serialization
  implicit val materializer = ActorMaterializer()
  val timeoutDuration = FiniteDuration(3, TimeUnit.SECONDS)

  def this() = this(ActorSystem("MollieCommandActorSpec"))

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  val config: MollieConfig = mollieConfig()

  "A MollieCommandActor" must {

    "be able to create an ideal payment" in {
      val testConnection: HttpServer = new HttpServer {
        override def sendRequest(request: HttpRequest): Future[HttpResponse] = {
          Unmarshal(request.entity)
            .to[JValue]
            .flatMap { requestJson =>
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
            }
          }
      }

      val commandActor = system.actorOf(
        MollieCommandActor.props(
          connection = testConnection,
          config = config
        )
      )


      commandActor ! CreatePaymentBanktransfer(
        amount = 10,
        description = "",
        redirectUrl = "http://redirect",
        webhookUrl = None,
        locale = Some(PaymentLocale.nl),
        metadata = Map("id" -> "some id")
      )

      expectMsgPF(timeoutDuration) {
        case resp: PaymentResponse => true
      }
    }
  }

}
