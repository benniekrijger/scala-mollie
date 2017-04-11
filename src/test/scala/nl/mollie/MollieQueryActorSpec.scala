package nl.mollie

import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.{StatusCodes, _}
import akka.stream.ActorMaterializer
import akka.testkit.{ImplicitSender, TestKit}
import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import nl.mollie.config.MollieConfig
import nl.mollie.connection.HttpServer
import nl.mollie.queries.{GetPayment, ListPaymentIssuers, ListPaymentMethods}
import nl.mollie.responses.{MollieFailure, PaymentIssuers, PaymentMethods, PaymentResponse}
import org.json4s.native.JsonMethods._
import org.json4s.{DefaultFormats, Formats, Serialization, jackson}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.concurrent.duration.FiniteDuration

class MollieQueryActorSpec(_system: ActorSystem) extends TestKit(_system) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll with Json4sSupport with MollieFactory {
  implicit val formats: Formats = DefaultFormats
  implicit val dispatcher: ExecutionContextExecutor = system.dispatcher
  implicit val jacksonSerialization: Serialization = jackson.Serialization
  implicit val materializer = ActorMaterializer()
  val timeoutDuration = FiniteDuration(3, TimeUnit.SECONDS)

  def this() = this(ActorSystem("MollieQueryActorSpec"))

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  val config: MollieConfig = mollieConfig()

  "A MollieQueryActor" must {

    "be able to find a payment" in {
      val testConnection: HttpServer = new HttpServer {
        override def sendRequest(request: HttpRequest): Future[HttpResponse] = {
          val json = parse(
            """
              {
                "id": "tr_WDqYK6vllg",
                "mode": "test",
                "createdDatetime": "2016-07-28T17:27:21.0Z",
                "status": "paid",
                "paidDatetime": "2016-07-28T17:32:04.0Z",
                "amount": 35.07,
                "description": "Order 33",
                "method": "ideal",
                "metadata": {
                    "order_id": "33"
                },
                "details": {
                    "consumerName": "Hr E G H K\u00fcppers en\/of MW M.J. K\u00fcppers-Veeneman",
                    "consumerAccount": "NL53INGB0618365937",
                    "consumerBic": "INGBNL2A"
                },
                "locale": "nl",
                "profileId": "pfl_QkEhN94Ba",
                "links": {
                    "webhookUrl": "https://webshop.example.org/payments/webhook",
                    "redirectUrl": "https://webshop.example.org/order/33/"
                }
              }
            """
          )

          Marshal(json)
            .to[MessageEntity]
            .map { entity =>
              HttpResponse(
                status = StatusCodes.OK,
                entity = entity
              )
            }
        }
      }

      val queryActor = system.actorOf(
        MollieQueryActor.props(
          connection = testConnection,
          config = config
        )
      )

      queryActor ! GetPayment(id = "paymentId")

      expectMsgPF(timeoutDuration) {
        case resp: PaymentResponse =>
      }
    }

    "return failure response on client exception when finding a payment" in {
      val testConnection = new HttpServer {
        override def sendRequest(request: HttpRequest): Future[HttpResponse] = Future.failed(new RuntimeException("Timeout"))
      }

      val queryActor = system.actorOf(
        MollieQueryActor.props(
          connection = testConnection,
          config = config
        )
      )

      queryActor ! GetPayment(id = "paymentId")

      expectMsgPF(timeoutDuration) {
        case _: MollieFailure =>
      }
    }

    "be able to list payments" in {
      val testConnection: HttpServer = new HttpServer {
        override def sendRequest(request: HttpRequest): Future[HttpResponse] = {
          val json = parse(
            """
              {
                "totalCount": 2,
                "offset": 0,
                "count": 2,
                "data": [
                  {
                    "id": "ideal",
                    "description": "iDEAL",
                    "amount": {
                      "minimum": "0.53",
                      "maximum": "50000.00"
                    },
                    "image": {
                      "normal": "https://www.mollie.com/images/payscreen/methods/ideal.png",
                      "bigger": "https://www.mollie.com/images/payscreen/methods/ideal@2x.png"
                    }
                  },
                  {
                    "id": "paypal",
                    "description": "PayPal",
                    "amount": {
                      "minimum": "0.13",
                      "maximum": "8000.00"
                    },
                    "image": {
                      "normal": "https://www.mollie.com/images/payscreen/methods/paypal.png",
                      "bigger": "https://www.mollie.com/images/payscreen/methods/paypal@2x.png"
                    }
                  }
                ]
              }
            """
          )

          Marshal(json)
            .to[MessageEntity]
            .map { entity =>
              HttpResponse(
                status = StatusCodes.OK,
                entity = entity
              )
            }
        }
      }

      val queryActor = system.actorOf(
        MollieQueryActor.props(
          connection = testConnection,
          config = config
        )
      )

      queryActor ! ListPaymentMethods()

      expectMsgPF(timeoutDuration) {
        case resp: PaymentMethods if resp.data.size == 2 =>
      }
    }

    "return failure response on client exception when listing payments" in {
      val testConnection = new HttpServer {
        override def sendRequest(request: HttpRequest): Future[HttpResponse] = Future.failed(new RuntimeException("Timeout"))
      }

      val queryActor = system.actorOf(
        MollieQueryActor.props(
          connection = testConnection,
          config = config
        )
      )

      queryActor ! ListPaymentMethods()

      expectMsgPF(timeoutDuration) {
        case _: MollieFailure =>
      }
    }

    "be able to list issuers" in {
      val testConnection: HttpServer = new HttpServer {
        override def sendRequest(request: HttpRequest): Future[HttpResponse] = {
          val json = parse(
            """
              {
                "totalCount": 9,
                "offset": 0,
                "count": 9,
                "data": [
                  {
                    "resource": "issuer",
                    "id": "ideal_ABNANL2A",
                    "name": "ABN AMRO",
                    "method": "ideal"
                  },
                  {
                    "resource": "issuer",
                    "id": "ideal_ASNBNL21",
                    "name": "ASN Bank",
                    "method": "ideal"
                  },
                  {
                    "resource": "issuer",
                    "id": "ideal_INGBNL2A",
                    "name": "ING",
                    "method": "ideal"
                  }
                ]
              }
            """
          )

          Marshal(json)
            .to[MessageEntity]
            .map { entity =>
              HttpResponse(
                status = StatusCodes.OK,
                entity = entity
              )
            }
        }
      }

      val queryActor = system.actorOf(
        MollieQueryActor.props(
          connection = testConnection,
          config = config
        )
      )

      queryActor ! ListPaymentIssuers()

      expectMsgPF(timeoutDuration) {
        case resp: PaymentIssuers if resp.data.size == 3 =>
      }
    }

    "return failure response on client exception when listing issuers" in {
      val testConnection = new HttpServer {
        override def sendRequest(request: HttpRequest): Future[HttpResponse] = Future.failed(new RuntimeException("Timeout"))
      }

      val queryActor = system.actorOf(
        MollieQueryActor.props(
          connection = testConnection,
          config = config
        )
      )

      queryActor ! ListPaymentIssuers()

      expectMsgPF(timeoutDuration) {
        case _: MollieFailure =>
      }
    }
  }

}
