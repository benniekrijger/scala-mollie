package nl.mollie.queries

case class ListPaymentMethods(
    offset: Option[Int] = None,
    count: Option[Int] = None
)
