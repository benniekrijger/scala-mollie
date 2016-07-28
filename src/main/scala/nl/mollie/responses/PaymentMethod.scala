package nl.mollie.responses

case class PaymentMethod(
    id: String,
    description: String,
    amount: PaymentMethodAmount,
    image: PaymentMethodImage
)

case class PaymentMethodImage(
    normal: String,
    bigger: String
)

case class PaymentMethodAmount(
    minimum: String,
    maximum: String
) {
    lazy val minAmount: BigDecimal = BigDecimal(minimum)
    lazy val maxAmount: BigDecimal = BigDecimal(maximum)
}
