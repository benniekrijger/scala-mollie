package nl.mollie.connection

import akka.http.scaladsl.model.{HttpRequest, HttpResponse}

import scala.concurrent.Future

trait HttpServer {

  def sendRequest(request: HttpRequest): Future[HttpResponse]

}
