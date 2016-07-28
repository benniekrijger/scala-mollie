Scala PostNL
============
[![Build Status](https://travis-ci.org/benniekrijger/scala-postnl.svg?branch=master)](https://travis-ci.org/benniekrijger/scala-postnl) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.benniekrijger/scala-postnl_2.11/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.benniekrijger/scala-postnl_2.11) [![License](http://img.shields.io/:license-mit-blue.svg)](http://doge.mit-license.org)

Scala client to connect to PostNL's SOAP service called CIF

Dependencies
------------
To include the latest release of the PostNL client into your `sbt` project, add the following lines to your `build.sbt` file:
```scala
   libraryDependencies += "com.github.benniekrijger" %% "scala-postnl" % "0.4"
```

This version of `scala-postnl` depends on Akka 2.4.8 and Scala 2.11.8. 


Sample usage
------------

```scala
   val postNLConfig = PostNLConfig(
     customerNumber = "YourCustomerNumber",
     customerCode = "YourCustomerCode",
     customerName = "BlueFlower.nl",
     username = "YourUsername",
     password = "YourPassword",
     collectionLocation = "YourCollectionLocation",
     globalPack = "YourGlobalPack",
     sandbox = true
   )
 
   val postNlClient: ActorRef = system.actorOf(
     PostNLClientActor.props(postNLConfig),
     PostNLClientActor.name
   )
 
   val receiverAddress = AddressType(
     addressType = AddressTypes.Receiver,
     firstName = Some("Jan"),
     name = "Smit",
     companyName = Some("Smit & Zonen"),
     street = "Hoofdstraat",
     houseNr = "1",
     houseNrExt = Some("A"),
     zipCode = "1234AB",
     city = "Heikant",
     countryCode = "NL"
   )
 
   val senderAddress = AddressType(
     addressType = AddressTypes.Sender,
     firstName = Some("Robert"),
     name = "Jansen",
     companyName = Some("Jansen & Janssen"),
     street = "Breeweg",
     houseNr = "19",
     houseNrExt = Some("c"),
     zipCode = "4401BN",
     city = "Yerseke",
     countryCode = "NL"
   )
 
   (postNlClient ? GenerateBarcodeByCountryCode("NL")).flatMap {
     case resp: BarcodeResponse =>
       system.log.info("Barcode: {}", resp)
 
       (postNlClient ? GenerateLabel(
         message = MessageType(printerType = PrinterTypes.ZebraLP2844Z),
         shipment = ShipmentType(
           addresses = Seq(receiverAddress, senderAddress),
           barcode = resp.barcode,
           dimension = DimensionType(
             weight = 1000
           ),
           productCodeDelivery = "3085"
         )
       )).map { response =>
         system.log.info("Label response: {}", response)
       }
     case msg =>
       system.log.error(s"Unknown barcode response: {}", msg)
       Future.successful(None)
   }
 ```
