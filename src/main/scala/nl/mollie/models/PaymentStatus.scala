package nl.mollie.models

object PaymentStatus extends Enumeration {
  val open: Value = Value("open")
  val cancelled: Value = Value("cancelled")
  val expired: Value = Value("expired")
  val pending: Value = Value("pending")
  val paid: Value = Value("paid")
  val paidout: Value = Value("paidout")
  val refunded: Value = Value("refunded")
  val charged_back: Value = Value("charged_back")
}
