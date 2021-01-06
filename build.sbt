libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % "2.6.6",
  "com.typesafe.akka" %% "akka-http" % "10.2.2",
  "net.ruippeixotog" %% "scala-scraper" % "1.2.0" exclude("xerces", "xercesImpl"),

  "org.scala-lang.modules" %% "scala-xml" % "1.2.0",
  "org.scalatest" %% "scalatest" % "3.1.2" % Test,
)
