package nl.mollie.config

import com.typesafe.config.Config

case class MollieConfig(
    apiHost: String,
    apiKey: String
)

object MollieConfig {
  def apply(config: Config): MollieConfig = {
    val c = config.getConfig("mollie")

    MollieConfig(
      apiHost = c.getString("apiHost"),
      apiKey = c.getString("apiKey")
    )
  }
}
