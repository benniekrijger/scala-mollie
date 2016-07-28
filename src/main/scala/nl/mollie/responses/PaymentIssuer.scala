package nl.mollie.responses

case class PaymentIssuer(
    id: String,
    name: String,
    method: String,
    offset: Option[Int],
    count: Option[Int]
)