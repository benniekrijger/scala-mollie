package nl.mollie

import nl.mollie.config.MollieConfig
import nl.mollie.responses.{PaymentLinks, PaymentResponse}

trait MollieFactory {

  def paymentLinks(
      paymentUrl: Option[String] = None,
      webhookUrl: Option[String] = None,
      redirectUrl: String = "http://redirect.nl",
      settlement: Option[String] = None
  ): PaymentLinks = {
    PaymentLinks(
      paymentUrl = paymentUrl,
      webhookUrl = webhookUrl,
      redirectUrl = redirectUrl,
      settlement = settlement
    )
  }

  def mollieConfig(
      apiHost: String = "host",
      apiBasePath: String = "/base",
      apiKey: Option[String] = Some("secret"),
      testApiKey: String = "testSecret",
      testMode: Boolean = false
  ): MollieConfig = {
    MollieConfig(
      apiHost = apiHost,
      apiBasePath = apiBasePath,
      apiKey = apiKey,
      testApiKey = testApiKey,
      testMode = testMode
    )
  }

  def paymentResponse(
      id: String = "1234",
      mode: String = "mode",
      createdDatetime: String = "1900-01-01 17:00",
      status: String = "payed",
      paidDatetime: Option[String] = None,
      cancelledDatetime: Option[String] = None,
      expiredDatetime: Option[String] = None,
      expiryPeriod: Option[String] = None,
      amount: String = "123",
      amountRefunded: Option[String] = None,
      amountRemaining: Option[String] = None,
      description: String = "description",
      method: Option[String] = None,
      locale: Option[String] = None,
      profileId: String = "id",
      settlementId: Option[String] = None,
      links: PaymentLinks = paymentLinks(),
      metadata: Map[String, String] = Map.empty
  ): PaymentResponse = {
    PaymentResponse(
      id = id,
      mode = mode,
      createdDatetime = createdDatetime,
      status = status,
      paidDatetime = paidDatetime,
      cancelledDatetime = cancelledDatetime,
      expiredDatetime = expiredDatetime,
      expiryPeriod = expiryPeriod,
      amount = amount,
      amountRefunded = amountRefunded,
      amountRemaining = amountRemaining,
      description = description,
      method = method,
      locale = locale,
      profileId = profileId,
      settlementId = settlementId,
      links = links,
      metadata = metadata
    )
  }

}
