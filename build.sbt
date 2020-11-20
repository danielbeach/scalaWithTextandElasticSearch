name := "theos"

version := "0.1"

scalaVersion := "2.13.3"

val AkkaVersion = "2.5.31"
libraryDependencies ++= Seq(
  "com.lightbend.akka" %% "akka-stream-alpakka-elasticsearch" % "2.0.2",
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion
)

val elastic4sVersion = "7.9.0"
libraryDependencies ++= Seq(
  // recommended client for beginners
  "com.sksamuel.elastic4s" %% "elastic4s-client-esjava" % elastic4sVersion,
  // test kit
  "com.sksamuel.elastic4s" %% "elastic4s-testkit" % elastic4sVersion % "test"
)

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.3" % Runtime
