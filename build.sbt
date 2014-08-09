import AssemblyKeys._ // put this at the top of the file

name := "calliope-example"

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
  "com.tuplejump" %% "calliope" % "0.9.4-EA-SNAPSHOT" % "provided",
  "org.apache.cassandra" % "cassandra-all" % "2.0.9" % "provided",
  "com.datastax.cassandra" % "cassandra-driver-core" % "2.0.2"  % "provided" exclude ("com.google.guava", "guava") ,
  "org.apache.spark" %% "spark-core" % "1.0.2" % "provided" exclude ("org.mortbay.jetty", "servlet-api") exclude ("org.eclipse.jetty.orbit", "javax.servlet"),
  "javax.servlet" % "javax.servlet-api" % "3.0.1" % "provided")

resolvers ++= Seq ("Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots")

assemblySettings
