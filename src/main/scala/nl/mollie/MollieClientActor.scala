package nl.mollie

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.routing.RoundRobinPool
import akka.stream.ActorMaterializer
import nl.mollie.commands.CreatePayment
import nl.mollie.config.MollieConfig
import nl.mollie.connection.{HttpServer, MollieConnection}
import nl.mollie.queries.{GetPayment, ListPaymentIssuers, ListPaymentMethods}

class MollieClientActor(config: MollieConfig) extends Actor with ActorLogging {
  log.info("Mollie client started")

  val connection: HttpServer = MollieConnection(config, context.system)

  def receive: Receive = {
    case cmd: CreatePayment =>
      commandClient forward cmd
    case qry: GetPayment =>
      queryClient forward qry
    case qry: ListPaymentMethods =>
      queryClient forward qry
    case qry: ListPaymentIssuers =>
      queryClient forward qry
  }

  private[this] def queryClient: ActorRef = {
    context.child(MollieQueryActor.name).getOrElse {
      context.actorOf(
        MollieQueryActor.props(
          connection = connection,
          config = config
        ).withRouter(RoundRobinPool(5)),
        MollieQueryActor.name
      )
    }
  }

  private[this] def commandClient: ActorRef = {
    context.child(MollieCommandActor.name).getOrElse {
      context.actorOf(
        MollieCommandActor.props(
          connection = connection,
          config = config
        ).withRouter(RoundRobinPool(5)),
        MollieCommandActor.name
      )
    }
  }

}

object MollieClientActor {

  final val name: String = "mollie"

  def props(): Props = Props(
    classOf[MollieClientActor]
  )

}
