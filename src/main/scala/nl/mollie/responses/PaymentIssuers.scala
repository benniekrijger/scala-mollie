package nl.mollie.responses

case class PaymentIssuers(
    totalCount: Int,
    offset: Int,
    count: Int,
    data: Seq[PaymentIssuer]
)
