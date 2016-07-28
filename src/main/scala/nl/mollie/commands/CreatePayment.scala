package nl.mollie.commands

import nl.mollie.models.PaymentLocale

trait CreatePayment {
  val amount: Double
  val description: String
  val redirectUrl: String
  val webhookUrl: Option[String]
  val method: String
  val locale: Option[PaymentLocale.Value]
  val metadata: Map[String, String]
}

case class CreatePaymentIdeal(
    issuer: Option[String] = None,
    amount: Double,
    description: String,
    redirectUrl: String,
    webhookUrl: Option[String],
    locale: Option[PaymentLocale.Value],
    metadata: Map[String, String] = Map.empty
) extends CreatePayment {
  val method: String = "ideal"
}

case class CreatePaymentCreditcard(
    billingAddress: Option[String] = None, //The card holder's info
    billingCity: Option[String] = None,
    billingRegion: Option[String] = None,
    billingPostal: Option[String] = None,
    billingCountry: Option[String] = None,
    shippingAddress: Option[String] = None, //The shipping address
    shippingCity: Option[String] = None,
    shippingRegion: Option[String] = None,
    shippingPostal: Option[String] = None,
    shippingCountry: Option[String] = None,
    amount: Double,
    description: String,
    redirectUrl: String,
    webhookUrl: Option[String],
    locale: Option[PaymentLocale.Value],
    metadata: Map[String, String] = Map.empty
) extends CreatePayment {
  val method: String = "creditcard"
}

case class CreatePaymentMistercash(
    amount: Double,
    description: String,
    redirectUrl: String,
    webhookUrl: Option[String],
    locale: Option[PaymentLocale.Value],
    metadata: Map[String, String] = Map.empty
) extends CreatePayment {
  val method: String = "mistercash"
}

case class CreatePaymentSofort(
    amount: Double,
    description: String,
    redirectUrl: String,
    webhookUrl: Option[String],
    locale: Option[PaymentLocale.Value],
    metadata: Map[String, String] = Map.empty
) extends CreatePayment {
  val method: String = "sofort"
}

case class CreatePaymentBanktransfer(
    billingEmail: Option[String] = None, // Consumer's e-mail address, to automatically send the bank transfer details to
    dueDate: Option[String] = None, // format YYYY-MM-DD, note: The minimum date is tomorrow and the maximum date is 100 days
    amount: Double,
    description: String,
    redirectUrl: String,
    webhookUrl: Option[String],
    locale: Option[PaymentLocale.Value],
    metadata: Map[String, String] = Map.empty
) extends CreatePayment {
  val method: String = "banktransfer"
}

case class CreatePaymentDirectDebit(
    consumerName: Option[String] = None, // Beneficiary name of the account holder.
    consumerAccount: Option[String] = None, // IBAN of the account holder.
    amount: Double,
    description: String,
    redirectUrl: String,
    webhookUrl: Option[String],
    locale: Option[PaymentLocale.Value],
    metadata: Map[String, String] = Map.empty
) extends CreatePayment {
  val method: String = "directdebit"
}

case class CreatePaymentBelfius(
    amount: Double,
    description: String,
    redirectUrl: String,
    webhookUrl: Option[String],
    locale: Option[PaymentLocale.Value],
    metadata: Map[String, String] = Map.empty
) extends CreatePayment {
  val method: String = "belfius"
}

case class CreatePaymentPaypal(
    shippingAddress: Option[String] = None,
    shippingCity: Option[String] = None,
    shippingRegion: Option[String] = None,
    shippingPostal: Option[String] = None,
    shippingCountry: Option[String] = None,
    amount: Double,
    description: String,
    redirectUrl: String,
    webhookUrl: Option[String],
    locale: Option[PaymentLocale.Value],
    metadata: Map[String, String] = Map.empty
) extends CreatePayment {
  val method: String = "paypal"
}

case class CreatePaymentBitcoin(
    amount: Double,
    description: String,
    redirectUrl: String,
    webhookUrl: Option[String],
    locale: Option[PaymentLocale.Value],
    metadata: Map[String, String] = Map.empty
) extends CreatePayment {
  val method: String = "bitcoin"
}

case class CreatePaymentPodiumcadeaukaart(
    amount: Double,
    description: String,
    redirectUrl: String,
    webhookUrl: Option[String],
    locale: Option[PaymentLocale.Value],
    metadata: Map[String, String] = Map.empty
) extends CreatePayment {
  val method: String = "podiumcadeaukaart"
}

case class CreatePaymentPaysafeCard(
    customerReference: Option[String] = None, // Used for consumer identification
    amount: Double,
    description: String,
    redirectUrl: String,
    webhookUrl: Option[String],
    locale: Option[PaymentLocale.Value],
    metadata: Map[String, String] = Map.empty
) extends CreatePayment {
  val method: String = "paysafecard"
}