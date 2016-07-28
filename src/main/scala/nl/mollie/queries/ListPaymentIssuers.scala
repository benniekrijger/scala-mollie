package nl.mollie.queries

case class ListPaymentIssuers(
    offset: Option[Int] = None,
    count: Option[Int] = None
)
