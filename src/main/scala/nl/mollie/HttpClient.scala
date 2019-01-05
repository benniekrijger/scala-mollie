package nl.mollie

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshalling.{Marshal, ToEntityMarshaller}
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.{FromEntityUnmarshaller, Unmarshal}
import akka.http.scaladsl.util.FastFuture
import akka.stream.Materializer

import scala.concurrent.{ExecutionContext, Future}

private[mollie] trait HttpClient {
  def sendRequest[B](
      uri: Uri,
      method: HttpMethod = HttpMethods.GET,
      entity: MessageEntity = HttpEntity.Empty,
      securityHeader: Option[HttpHeader] = None
  )(implicit m: FromEntityUnmarshaller[B]): Future[Either[RequestFailure, B]]

  def sendCommand[A, B](
      uri: Uri,
      httpMethod: HttpMethod = HttpMethods.POST,
      command: A,
      securityHeader: Option[HttpHeader] = None
  )(implicit mA: ToEntityMarshaller[A], mB: FromEntityUnmarshaller[B]): Future[Either[RequestFailure, B]]
}

private[mollie] class HttpClientImpl()(implicit system: ActorSystem, materializer: Materializer, ec: ExecutionContext) extends HttpClient {
  private val log: LoggingAdapter = Logging(system, this.getClass)
  private val http = Http()

  def sendRequest[B](
      path: Uri,
      method: HttpMethod = HttpMethods.GET,
      entity: MessageEntity = HttpEntity.Empty,
      securityHeader: Option[HttpHeader] = None
  )(implicit m: FromEntityUnmarshaller[B]): Future[Either[RequestFailure, B]] = {
    log.info("sendRequest: [{}] {}", method.value, path)

    val request = HttpRequest(
      uri = path,
      method = method,
      entity = entity,
      headers = List(
        Some(headers.Accept(MediaTypes.`application/json`)),
        securityHeader
      ).flatten
    )

    http.singleRequest(request).flatMap(handleResponse(request, _))
  }

  def sendCommand[A, B](
      path: Uri,
      httpMethod: HttpMethod = HttpMethods.POST,
      command: A,
      securityHeader: Option[HttpHeader] = None
  )(implicit mA: ToEntityMarshaller[A], mB: FromEntityUnmarshaller[B]): Future[Either[RequestFailure, B]] = {
    Marshal(command).to[MessageEntity].flatMap { entity =>
      sendRequest(
        path = path,
        entity = entity,
        method = httpMethod,
        securityHeader = securityHeader
      )
    }
  }

  private def handleResponse[B](request: HttpRequest, response: HttpResponse)(implicit mB: FromEntityUnmarshaller[B]): Future[Either[RequestFailure, B]] = {
    response match {
      case HttpResponse(code, _, entity, _) if HttpClientImpl.validResponseCodes.contains(code) =>
        Unmarshal(entity).to[B].map(Right(_)).recover {
          case t: Throwable =>
            entity.discardBytes()
            log.error(t, "handleResponse: failure")
            Left(RequestFailure("failed to handle response"))
        }
      case resp: HttpResponse =>
        log.error("request failed, request={}, response={}", request.toString(), response.toString())
        resp.discardEntityBytes()

        FastFuture.successful(Left(RequestFailure("request failed")))
    }
  }
}

object HttpClientImpl {
  private val validResponseCodes: Set[StatusCode] = Set(StatusCodes.OK, StatusCodes.Created, StatusCodes.Accepted)
}