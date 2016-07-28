package nl.mollie.models

object PaymentStatus extends Enumeration {
  val open = Value("open")
  val cancelled = Value("cancelled")
  val expired = Value("expired")
  val pending = Value("pending")
  val paid = Value("paid")
  val paidout = Value("paidout")
  val refunded = Value("refunded")
  val charged_back = Value("charged_back")
}