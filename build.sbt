
organization := "name.heikoseeberger.sbtsrc"

name := "sbtsrc"

sbtPlugin := true

libraryDependencies += "org.scalaz" %% "scalaz-core" % "6.0.3"

scalacOptions ++= Seq("-unchecked", "-deprecation")

publishTo <<= (version) { version =>
  def hseeberger(name: String) =
    Resolver.file("hseeberger-%s" format name, file("/Users/heiko/projects/hseeberger.github.com/%s" format name))(Resolver.ivyStylePatterns)
  val resolver =
    if (version endsWith "SNAPSHOT") hseeberger("snapshots")
    else hseeberger("releases")
  Option(resolver)
}

publishMavenStyle := false
