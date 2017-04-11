Scala Mollie
============
[![Build Status](https://travis-ci.org/benniekrijger/scala-mollie.svg?branch=master)](https://travis-ci.org/benniekrijger/scala-mollie) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.benniekrijger/scala-mollie_2.11/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.benniekrijger/scala-mollie_2.11) [![License](http://img.shields.io/:license-mit-blue.svg)](http://doge.mit-license.org)

Scala client to connect to the Mollie payment provider API

Dependencies
------------
To include the latest release of the Mollie client into your `sbt` project, add the following lines to your `build.sbt` file:
```scala
   libraryDependencies += "com.github.benniekrijger" %% "scala-mollie" % "0.14"
```

This version of `scala-mollie` depends on Akka 2.4.14 and Scala 2.12.0. 


Sample usage
------------

```scala

     val mollieConfig = MollieConfig(
       apiHost = "api.mollie.nl",
       apiBasePath = "v1",
       apiKey = Some("liveApiKey"),
       testApiKey = "testApiKey",
       testMode = true
     )
   
     val mollieClient = system.actorOf(
       MollieClientActor.props(mollieConfig),
       MollieClientActor.name
     )
   
     (mollieClient ? GetPayment("some-payment-id")) {
       case resp: PaymentResponse =>
       case _ => // failure
     }
   
     (mollieClient ? ListPaymentIssuers()) {
       case resp: PaymentIssuers =>
       case _ => // failure
     }
   
     (mollieClient ? ListPaymentMethods()) {
       case resp: PaymentMethods =>
       case _ => // failure
     }
     
     (mollieClient ? CreatePaymentIdeal(
        issuer = "ideal_RABONL",
        amount = 10.5,
        description = "Test payment",
        redirectUrl = "http://example.nl/return-url.html",
        webhookUrl = Some("http://example.nl/webhook.html"),
        locale = Some("nl"),
        metadata = Map("orderId" -> "1234")
     )) {
       case resp: PaymentResponse =>
       case _ => // failure
     }
     
 ```
