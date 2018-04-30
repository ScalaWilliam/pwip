scalaVersion := "2.12.6"

name := "pwip"

enablePlugins(PlayScala)
enablePlugins(DockerPlugin)

libraryDependencies ++= Seq(
  filters,
  logback,
  "com.typesafe.play" %% "play-json" % "2.6.9",
  "org.scalatest" %% "scalatest" % "3.0.5" % Test,
  "com.vladsch.flexmark" % "flexmark-all" % "0.32.22",
  "org.eclipse.jgit" % "org.eclipse.jgit" % "4.11.0.201803080745-r",
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test,
  "org.seleniumhq.selenium" % "selenium-java" % "3.11.0" % Test,
  "org.seleniumhq.selenium" % "htmlunit-driver" % "2.30.0" % Test,
// todo take screenshots with Firefox as part of documentation or something
  // so we can have automated docs :-D
  "org.seleniumhq.selenium" % "selenium-firefox-driver" % "3.11.0" % Test,
  "org.jsoup" % "jsoup" % "1.11.3"
)
