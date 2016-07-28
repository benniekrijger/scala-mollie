package nl.mollie.responses

case class PaymentResponse(
    id: String,
    mode: String,
    createdDatetime: String,
    status: String,
    paidDatetime: Option[String],
    cancelledDatetime: Option[String],
    expiredDatetime: Option[String],
    expiryPeriod: Option[String],
    amount: String,
    amountRefunded: Option[String],
    amountRemaining: Option[String],
    description: String,
    method: Option[String],
    locale: Option[String],
    profileId: String,
    settlementId: Option[String],
    links: PaymentLinks,
    metadata: Map[String, String]
)

case class PaymentLinks(
    paymentUrl: Option[String],
    webhookUrl: Option[String],
    redirectUrl: String,
    settlement: Option[String]
)
