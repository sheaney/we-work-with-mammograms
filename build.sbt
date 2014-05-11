import play.Project._

import net.litola.SassPlugin

name := "we-work-with-mammograms"

testOptions in Test := Nil

compile in Test <<= PostCompile(Test)

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  "com.google.inject" % "guice" % "3.0",
  "javax.inject" % "javax.inject" % "1",
  "org.mockito" % "mockito-core" % "1.9.5" % "test",
  "org.jsoup" % "jsoup" % "1.7.2" % "test",
  "org.scalatest" % "scalatest_2.10" % "2.1.3" % "test",
  "postgresql" % "postgresql" % "9.1-901.jdbc4",
  "org.webjars" %% "webjars-play" % "2.2.0",
  "org.webjars" % "bootstrap" % "2.3.1",
  "be.objectify" %% "deadbolt-java" % "2.2-RC4",
  "com.amazonaws" % "aws-java-sdk" % "1.7.7"
)

playJavaSettings ++ SassPlugin.sassSettings

initialCommands in console += """
  import views.html._
  import models._
  import controllers.Application._
  import play.mvc.Controller.session
  import play.libs.Json
  new play.core.StaticApplication(new java.io.File("."))
"""

resolvers += Resolver.url("Objectify Play Repository", url("http://schaloner.github.com/releases/"))(Resolver.ivyStylePatterns)

incOptions := incOptions.value.withNameHashing(true)
