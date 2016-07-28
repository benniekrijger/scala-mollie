package nl.mollie.connection

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.OutgoingConnection
import akka.http.scaladsl.model.{HttpHeader, HttpRequest, HttpResponse, headers}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink, Source}
import com.sun.media.sound.InvalidDataException
import nl.mollie.config.MollieConfig

import scala.concurrent.Future

case class MollieConnection(
    config: MollieConfig,
    actorSystem: ActorSystem
) extends HttpServer {

  implicit val system = actorSystem
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val authorizationHeader: HttpHeader = headers.Authorization(
    headers.OAuth2BearerToken(token = config.testMode match {
      case false if config.apiKey.isDefined => config.apiKey.get
      case false => throw new InvalidDataException("Mollie ApiKey not defined")
      case true => config.testApiKey
    })
  )

  val http: Flow[HttpRequest, HttpResponse, Future[OutgoingConnection]] =
    Http().outgoingConnectionHttps(host = config.apiHost)

  override def sendRequest(request: HttpRequest): Future[HttpResponse] = {
    Source.single(request.withHeaders(authorizationHeader))
      .via(http)
      .runWith(Sink.head)
  }

}
