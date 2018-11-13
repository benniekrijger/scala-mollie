package nl.mollie

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{HttpHeader, Uri, headers}
import akka.stream.Materializer
import com.sun.media.sound.InvalidDataException
import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import nl.mollie.commands.CreatePayment
import nl.mollie.config.MollieConfig
import nl.mollie.models.{PaymentMode, PaymentStatus}
import nl.mollie.responses.{PaymentIssuers, PaymentMethods, PaymentResponse}
import org.json4s.ext.{EnumNameSerializer, JavaTypesSerializers, JodaTimeSerializers}
import org.json4s.jackson.Serialization
import org.json4s.{DefaultFormats, Formats, jackson}

import scala.concurrent.{ExecutionContext, Future}

trait MollieClient {
  def createPayment(cmd: CreatePayment): Future[Either[RequestFailure, PaymentResponse]]
  def getPayment(id: String): Future[Either[RequestFailure, PaymentResponse]]
  def listPaymentMethods(offset: Option[Int] = None, count: Option[Int] = None): Future[Either[RequestFailure, PaymentMethods]]
  def listPaymentIssuers(offset: Option[Int] = None, count: Option[Int] = None): Future[Either[RequestFailure, PaymentIssuers]]
}

object MollieClient {
  def apply(config: MollieConfig)(implicit system: ActorSystem, materializer: Materializer, ec: ExecutionContext): MollieClient = {
    new MollieClientImpl(
      config = config,
      httpClient = new HttpClientImpl()
    )
  }
}

final class MollieClientImpl(
    config: MollieConfig,
    httpClient: HttpClient
)(implicit system: ActorSystem, ec: ExecutionContext) extends MollieClient with Json4sSupport {
  private implicit val baseFormats: Formats = {
    DefaultFormats ++ JavaTypesSerializers.all ++ JodaTimeSerializers.all + new EnumNameSerializer(PaymentStatus) + new EnumNameSerializer(PaymentMode)
  }
  private implicit val baseSerialization: Serialization.type = jackson.Serialization

  private val authorizationHeader: HttpHeader = headers.Authorization(
    headers.OAuth2BearerToken(token = config.testMode match {
      case false if config.apiKey.isDefined => config.apiKey.get
      case false => throw new InvalidDataException("Mollie ApiKey not defined")
      case true => config.testApiKey
    })
  )

  def createPayment(cmd: CreatePayment): Future[Either[RequestFailure, PaymentResponse]] = {
    val uri = Uri(config.apiHost)
      .withScheme("https")
      .withPath(Uri.Path("/v1/payments"))

    httpClient.sendCommand[CreatePayment, PaymentResponse](
      uri = uri,
      command = cmd,
      securityHeader = Some(authorizationHeader)
    )
  }

  def getPayment(id: String): Future[Either[RequestFailure, PaymentResponse]] = {
    val uri = Uri(config.apiHost)
      .withScheme("https")
      .withPath(Uri.Path("/v1/payments/" + id))

    httpClient.sendRequest[PaymentResponse](
      uri = uri,
      securityHeader = Some(authorizationHeader)
    )
  }

  def listPaymentMethods(offset: Option[Int] = None, count: Option[Int] = None): Future[Either[RequestFailure, PaymentMethods]] = {
    val params = Seq(
      count.map(c => "count" -> c.toString),
      offset.map(o => "offset" -> o.toString)
    ).flatten

    val uri = Uri(config.apiHost)
      .withScheme("https")
      .withPath(Uri.Path("/v1/methods"))
      .withQuery(Uri.Query(params:_*))

    httpClient.sendRequest[PaymentMethods](
      uri = uri,
      securityHeader = Some(authorizationHeader)
    )
  }

  def listPaymentIssuers(offset: Option[Int] = None, count: Option[Int] = None): Future[Either[RequestFailure, PaymentIssuers]] = {
    val params = Seq(
      count.map(c => "count" -> c.toString),
      offset.map(o => "offset" -> o.toString)
    ).flatten

    val uri = Uri(config.apiHost)
      .withScheme("https")
      .withPath(Uri.Path("/v1/issuers"))
      .withQuery(Uri.Query(params:_*))

    httpClient.sendRequest[PaymentIssuers](
      uri = uri,
      securityHeader = Some(authorizationHeader)
    )
  }

}
