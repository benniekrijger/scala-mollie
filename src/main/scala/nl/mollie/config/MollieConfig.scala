package nl.mollie.config

case class MollieConfig(
    apiHost: String,
    apiBasePath: String,
    apiKey: Option[String],
    testApiKey: String,
    testMode: Boolean
) {
  require(!testMode || (testMode && apiKey.isDefined), "In live mode api key needs to be defined!")
}
