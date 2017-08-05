import ScalaModulePlugin._

scalaModuleSettings

scalaVersionsByJvm in ThisBuild := {
  val v211 = "2.11.11"
  val v212 = "2.12.2"

  Map(
    6 -> List(v211 -> true),
    7 -> List(v211 -> false),
    8 -> List(v212 -> true, v211 -> false))
}

javacOptions ++= Seq("-source", "1.8")

val scalaV = "2.12.1"
val akkaV = "2.4.17"
val akkaHttpV = "10.0.3"
val slickV = "3.2.0"
val slickJodaMapperV = "2.3.0"
val slickForkliftV = "0.3.0"
val slickLessV = "0.3.2"
val redisClientV = "3.4"
val json4sV = "3.5.3"

// libraries
val scalaAsync = Seq (
  "org.scala-lang.modules" %% "scala-async" % "0.9.6"
)

val redis = Seq (
  "net.debasishg" %% "redisclient" % redisClientV
)

val slick = Seq (
  "com.typesafe.slick" %% "slick" % slickV,
  "com.typesafe.slick" %% "slick-hikaricp" % slickV,
  "com.liyaos" %% "scala-forklift-slick" % slickForkliftV,
  "io.underscore" %% "slickless" % slickLessV,
  "com.github.tototoshi" %% "slick-joda-mapper" % slickJodaMapperV,
  "mysql" % "mysql-connector-java" % "5.1.38"
)

val akka = Seq (
  "com.typesafe.akka" %% "akka-http" % akkaHttpV exclude("org.slf4j","slf4j-log4j12"),
  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpV,
  "com.typesafe.akka" %% "akka-cluster" % akkaV,
  "com.typesafe.akka" %% "akka-cluster-metrics" % akkaV,
  "com.typesafe.akka" %% "akka-cluster-tools" % akkaV,
  "com.typesafe.akka" %% "akka-slf4j" % akkaV
)

val testKit = Seq (
  "org.scalactic" %% "scalactic" % "latest.integration" % Test,
  "org.scalatest" %% "scalatest" % "latest.integration" % Test,
  "org.scalamock" %% "scalamock-scalatest-support" % "latest.integration" % Test
)

val joadTime = Seq (
  "org.joda" % "joda-convert" % "1.8.1",
  "joda-time" % "joda-time" % "2.9.2"
)

val json4s = Seq (
  "org.json4s" %% "json4s-native" % json4sV,
  "org.json4s" %% "json4s-ext" % json4sV
)

val commonSettings = Seq (
  name := "concurrent-in-scala",
  organization := "com.cody.chen",
  version := "1.0.0",
  scalaVersion := scalaV,
  resolvers := Seq(
    "aliyun" at "http://maven.aliyun.com/nexus/content/groups/public/",
//    "jcenter" at "http://jcenter.bintray.com/",
    Resolver.jcenterRepo,
//    "Local Maven" at s"file://${Path.userHome.absolutePath}/.m2/repository",
    Resolver.defaultLocal,
    Resolver.mavenLocal,
    DefaultMavenRepository
  ),
  externalResolvers := Resolver.withDefaultResolvers(resolvers.value, mavenCentral = false),
  assemblyMergeStrategy in assembly := {
    case n if n.startsWith("META-INF\\MANIFEST.MF") => MergeStrategy.discard
    case PathList("javax", "servlet", xs @ _*) => MergeStrategy.first
    case PathList("javax", "xml","stream", xs @ _*) => MergeStrategy.first
    case PathList("net", "sf","cglib", xs @ _*) => MergeStrategy.first
    case PathList("org", "apache","commons", xs @ _*) => MergeStrategy.first
    case PathList("jline", xs @ _*) => MergeStrategy.first
    case PathList("META-INF", "MANIFEST.MF", xs @ _*) => MergeStrategy.discard
    case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
    case PathList("com", "esotericsoftware","minlog", xs @ _*) => MergeStrategy.first
    case PathList("org", xs @ _*) => MergeStrategy.first
    case PathList("javax", "activation", xs @ _*) => MergeStrategy.first
    case PathList(ps @ _*) if ps.last.endsWith(".html") || ps.last.contains("MANIFEST.MF") => MergeStrategy.first
    case PathList("META-INF", "io.netty.versions.properties") => MergeStrategy.discard
    case "unwanted.txt" => MergeStrategy.discard
    case "MANIFEST.MF" => MergeStrategy.first
    case "META-INF/MANIFEST.MF" => MergeStrategy.discard
    case "application.conf" => MergeStrategy.concat
    case x =>
      val oldStrategy = (assemblyMergeStrategy in assembly).value
      oldStrategy(x)
  },
  testOptions += Tests.Argument(TestFrameworks.ScalaTest)
)


lazy val futureModel = (project in file("future-model"))
  .settings(commonSettings)
  .settings(
    name := "future-model",
    libraryDependencies ++= Seq(
      "de.heikoseeberger" %% "akka-http-json4s" % "1.12.0",
      "org.fusesource" % "sigar" % "1.6.4",
      "io.kamon" % "sigar-loader" % "1.6.6-rev002",
      "ch.qos.logback" % "logback-classic" % "1.1.3"
    ),
    libraryDependencies ++= (scalaAsync ++ joadTime ++ json4s ++ testKit)
//    mainClass in assembly := Some("Main"),
//    assemblyJarName in assembly := s"${name.value}-${version.value}.jar"
  )



lazy val actorModel = (project in file("actor-model"))
  .settings(commonSettings)
  .settings(
    name := "actor-model",
    libraryDependencies ++= Seq(
      "de.heikoseeberger" %% "akka-http-json4s" % "1.12.0",
      "org.fusesource" % "sigar" % "1.6.4",
      "io.kamon" % "sigar-loader" % "1.6.6-rev002",
      "ch.qos.logback" % "logback-classic" % "1.1.3"
    ),
    libraryDependencies ++= (akka ++ scalaAsync ++ joadTime ++ json4s ++ testKit)
  //    mainClass in assembly := Some("Main"),
  //    assemblyJarName in assembly := s"${name.value}-${version.value}.jar"
)


lazy val stmModel = (project in file("stm-model"))
.settings(commonSettings)
.settings(
  name := "stm-model",
  libraryDependencies ++= (scalaAsync ++ joadTime ++json4s ++ testKit)
)


val customTask = taskKey[Unit]("example")

val customTaskVal = customTask := {
 println("A new custom task!")
}

lazy val root = (project in file("."))
  .aggregate(futureModel, actorModel, stmModel)
  .settings(
    name := "root",
    customTaskVal
  )

