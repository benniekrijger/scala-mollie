package nl.mollie.config

import com.typesafe.config.Config

case class MollieConfig(
    apiHost: String,
    apiKey: Option[String],
    testApiKey: String,
    testMode: Boolean
) {
  require(testMode || (!testMode && apiKey.isDefined), "In live mode api key needs to be defined!")
}

object MollieConfig {
  def apply(config: Config): MollieConfig = {
    val c = config.getConfig("mollie")

    MollieConfig(
      apiHost = c.getString("apiHost"),
      apiKey = if (c.hasPath("apiKey")) Some(c.getString("apiKey")) else None,
      testApiKey = c.getString("testApiKey"),
      testMode = c.getBoolean("testMode")
    )
  }
}
