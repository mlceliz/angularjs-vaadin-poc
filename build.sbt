name := "Vaadin Scala project"
 
scalaVersion := "2.10.3"
 
seq(webSettings: _*)

// basic dependencies
libraryDependencies ++= Seq(
  "com.vaadin" % "vaadin" % "6.8.2",
  "org.scalatra" %% "scalatra" % "2.2.2",
  "org.scalatra" %% "scalatra-json" % "2.2.2",
  "org.scalatra" %% "scalatra-scalate" % "2.2.2",
  "org.json4s"   %% "json4s-jackson" % "3.2.4",
  "javax.servlet" % "servlet-api" % "2.5" % "provided",
  "org.eclipse.jetty" % "jetty-webapp" % "9.1.0.v20131115" % "container",
  "org.eclipse.jetty" % "jetty-plus" % "9.1.0.v20131115" % "container"
)