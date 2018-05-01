scalaVersion := "2.12.6"

name := "pwip"

libraryDependencies in ThisBuild += "org.scalatest" %% "scalatest" % "3.0.5" % Test

enablePlugins(PlayScala)
enablePlugins(DockerPlugin)

lazy val `page-store` = project
  .settings(
    libraryDependencies += "org.eclipse.jgit" % "org.eclipse.jgit" % "4.11.0.201803080745-r",
  )

dependsOn(`page-store` % "test->test;compile->compile")

libraryDependencies ++= Seq(
  filters,
  logback,
  "com.typesafe.play" %% "play-json" % "2.6.9",
  "com.vladsch.flexmark" % "flexmark-all" % "0.32.22",
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test,
  "org.seleniumhq.selenium" % "selenium-java" % "3.11.0" % Test,
  "org.seleniumhq.selenium" % "htmlunit-driver" % "2.30.0" % Test,
// todo take screenshots with Firefox as part of documentation or something
  // so we can have automated docs :-D
  "org.seleniumhq.selenium" % "selenium-firefox-driver" % "3.11.0" % Test,
  "org.jsoup" % "jsoup" % "1.11.3",
)
