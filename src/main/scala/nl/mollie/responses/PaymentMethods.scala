package nl.mollie.responses

case class PaymentMethods(
    totalCount: Int,
    offset: Int,
    count: Int,
    data: Seq[PaymentMethod]
)
