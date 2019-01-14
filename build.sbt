import sbt.Keys._
import com.amazonaws.regions.{Region, Regions}
import com.typesafe.sbt.packager.docker._
import com.typesafe.sbt.packager.docker.DockerPlugin.autoImport.DockerAlias

import scala.language.postfixOps

name := "practice"

organization := "com.example"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.12.8"

fork in run := true

resolvers += Resolver.sonatypeRepo("releases")
resolvers += Resolver.jcenterRepo

enablePlugins(
  JavaAppPackaging,
  DockerPlugin,
  GitVersioning,
  GitBranchPrompt,
  DockerContainerPlugin,
  MicrositesPlugin,
  EcrPlugin
)

initialCommands in console := """
                | import com.twitter.util.{Future, FuturePool, Await}
                |""".stripMargin

coverageHighlighting := true

coverageMinimum := 70
coverageFailOnMinimum := true
coverageExcludedPackages := ".*sse*.;.*util*.;.*client*."

scapegoatVersion in ThisBuild := "1.3.8"

scalafmtConfig := Some(file(".scalafmt.conf"))
scalafmtOnCompile := true

autoCompilerPlugins := true
addCompilerPlugin("com.criteo.socco"      %% "socco-plugin"       % "0.1.9")
addCompilerPlugin("com.olegpy"            %% "better-monadic-for" % "0.3.0-M4")
addCompilerPlugin("com.github.cb372"      %% "scala-typed-holes"  % "0.0.3")
addCompilerPlugin("com.softwaremill.neme" %% "neme-plugin"        % "0.0.1")
addCompilerPlugin(("io.tryp" % "splain" % "0.3.5").cross(CrossVersion.patch))

lazy val versions = new {
  val finatra        = "19.1.0"
  val guice          = "4.2.2"
  val logback        = "1.2.3"
  val mockito        = "1.10.19"
  val scalatest      = "3.0.5"
  val junitInterface = "0.11"
  val dockerItScala  = "0.9.8"
  val scalaUri       = "1.3.1"
  val hamsters       = "2.6.0"
  val fluentdScala   = "0.2.5"
  val swaggerFinatra = "18.11.0"
  val wireMock       = "2.19.0"
  val catbird        = "19.1.0"
  val scalaErrors    = "1.2"
  val perfolation    = "1.0.4"
  val mouse          = "0.20"
  val reactivemongo  = "0.13.0"
  val monix          = "3.0.0-fbcb270"
  val rideId         = "1.0.1"
}

libraryDependencies ++= Seq(
  "com.github.kolotaev"          %% "ride"                            % versions.rideId,
  "io.monix"                     %% "monix-execution"                 % versions.monix,
  "com.jakehschwartz"            %% "finatra-swagger"                 % versions.swaggerFinatra,
  "org.typelevel"                %% "mouse"                           % versions.mouse,
  "com.outr"                     %% "perfolation"                     % versions.perfolation,
  "com.github.mehmetakiftutuncu" %% "errors"                          % versions.scalaErrors,
  "io.catbird"                   %% "catbird-finagle"                 % versions.catbird,
  "io.catbird"                   %% "catbird-effect"                  % versions.catbird,
  "com.github.tomakehurst"       % "wiremock"                         % versions.wireMock,
  "eu.inn"                       %% "fluentd-scala"                   % versions.fluentdScala,
  "io.github.scala-hamsters"     %% "hamsters"                        % versions.hamsters,
  "io.lemonlabs"                 %% "scala-uri"                       % versions.scalaUri,
  "com.twitter"                  %% "finatra-http"                    % versions.finatra,
  "com.twitter"                  %% "finatra-httpclient"              % versions.finatra,
  "com.twitter"                  %% "finatra-jackson"                 % versions.finatra,
  "ch.qos.logback"               % "logback-classic"                  % versions.logback,
  "com.twitter"                  %% "twitter-server-logback-classic"  % versions.finatra,
  "org.reactivemongo"            %% "reactivemongo"                   % versions.reactivemongo,
  "com.twitter"                  %% "finatra-http"                    % versions.finatra % "test",
  "com.twitter"                  %% "finatra-jackson"                 % versions.finatra % "test",
  "com.twitter"                  %% "inject-server"                   % versions.finatra % "test",
  "com.twitter"                  %% "inject-app"                      % versions.finatra % "test",
  "com.twitter"                  %% "inject-core"                     % versions.finatra % "test",
  "com.twitter"                  %% "inject-modules"                  % versions.finatra % "test",
  "com.google.inject.extensions" % "guice-testlib"                    % versions.guice % "test",
  "com.twitter"                  %% "finatra-http"                    % versions.finatra % "test" classifier "tests",
  "com.twitter"                  %% "finatra-jackson"                 % versions.finatra % "test" classifier "tests",
  "com.twitter"                  %% "inject-server"                   % versions.finatra % "test" classifier "tests",
  "com.twitter"                  %% "inject-app"                      % versions.finatra % "test" classifier "tests",
  "com.twitter"                  %% "inject-core"                     % versions.finatra % "test" classifier "tests",
  "com.twitter"                  %% "inject-modules"                  % versions.finatra % "test" classifier "tests",
  "org.mockito"                  % "mockito-core"                     % versions.mockito % "test",
  "org.scalatest"                %% "scalatest"                       % versions.scalatest % "test",
  "com.novocode"                 % "junit-interface"                  % versions.junitInterface % "test",
  "com.whisk"                    %% "docker-testkit-scalatest"        % versions.dockerItScala % "test",
  "com.whisk"                    %% "docker-testkit-impl-docker-java" % versions.dockerItScala % "test"
)

testOptions += Tests.Argument(TestFrameworks.JUnit, "-q", "-v")

fork in test := false

parallelExecution in Test := false

clippyColorsEnabled := true

scalacOptions ++= Seq(
  "-target:jvm-1.8",
  "-encoding",
  "UTF-8",
  "-unchecked",
  "-language:existentials",
  "-language:experimental.macros",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-deprecation",
  "-explaintypes",
  "-feature",
  "-Xcheckinit",
  "-Xlint:adapted-args", // Warn if an argument list is modified to match the receiver.
  "-Xlint:by-name-right-associative", // By-name parameter of right associative operator.
  "-Xlint:constant", // Evaluation of a constant arithmetic expression results in an error.
  "-Xlint:delayedinit-select", // Selecting member of DelayedInit.
  "-Xlint:doc-detached", // A Scaladoc comment appears to be detached from its element.
  "-Xlint:inaccessible", // Warn about inaccessible types in method signatures.
  "-Xlint:infer-any", // Warn when a type argument is inferred to be `Any`.
  "-Xlint:missing-interpolator", // A string literal appears to be missing an interpolator id.
  "-Xlint:nullary-override", // Warn when non-nullary `def f()' overrides nullary `def f'.
  "-Xlint:nullary-unit", // Warn when nullary methods return Unit.
  "-Xlint:option-implicit", // Option.apply used implicit view.
  "-Xlint:package-object-classes", // Class or object defined in package object.
  "-Xlint:poly-implicit-overload", // Parameterized overloaded implicit methods are not visible as view bounds.
  "-Xlint:private-shadow", // A private field (or class parameter) shadows a superclass field.
  "-Xlint:stars-align", // Pattern sequence wildcard must align with sequence component.
  "-Xlint:type-parameter-shadow", // A local type parameter shadows a type already in scope.
  "-Xlint:unsound-match", // Pattern match may not be typesafe.
  "-Yno-adapted-args", // Do not adapt an argument list (either by inserting () or creating a tuple) to match the receiver.
  "-Ypartial-unification", // Enable partial unification in type constructor inference
  "-Ywarn-dead-code", // Warn when dead code is identified.
  "-Ywarn-extra-implicit", // Warn when more than one implicit parameter section is defined.
  "-Ywarn-inaccessible", // Warn about inaccessible types in method signatures.
  "-Ywarn-infer-any", // Warn when a type argument is inferred to be `Any`.
  "-Ywarn-nullary-override", // Warn when non-nullary `def f()' overrides nullary `def f'.
  "-Ywarn-nullary-unit", // Warn when nullary methods return Unit.
  "-Ywarn-numeric-widen", // Warn when numerics are widened.
  "-Ywarn-unused:implicits", // Warn if an implicit parameter is unused.
  "-Ywarn-unused:imports", // Warn if an import selector is not referenced.
  "-Ywarn-unused:locals", // Warn if a local definition is unused.
  "-Ywarn-unused:params", // Warn if a value parameter is unused.
  "-Ywarn-unused:patvars", // Warn if a variable bound in a pattern is unused.
  "-Ywarn-unused:privates", // Warn if a private member is unused.
  "-Ywarn-value-discard", // Warn when non-Unit expression results are unused.
  "-P:clippy:colors=true",
  "-Ycache-plugin-class-loader:last-modified",
  "-Ycache-macro-class-loader:last-modified",
  "-Ybackend-parallelism",
  s"${sys.runtime.availableProcessors() * 2}",
  "-Ybackend-worker-queue",
  "8",
  "-P:bm4:no-filtering:y",
  "-P:bm4:no-map-id:y",
  "-P:bm4:no-tupling:y",
  "-P:socco:out:./target/socco",
  "-P:socco:package_com.twitter.util:https://twitter.github.io/util/docs/",
  "-P:socco:package_scala:http://www.scala-lang.org/api/current/",
  "-P:socco:package_com.htc.vr8.:file://./target/scala-2.2/api/",
  "-P:splain:all:true"
)

// bashScriptExtraDefines += """addJava "-Dnetworkaddress.cache.ttl=60""""
bashScriptExtraDefines ++= Seq(
  """addJava "-server"""",
  """addJava "-Dnetworkaddress.cache.ttl=60"""",
  """addJava "-XX:+UnlockExperimentalVMOptions"""",
  """addJava "-XX:+UseCGroupMemoryLimitForHeap"""",
  """addJava "-XX:+UseG1GC"""",
  """addJava "-XX:+UseStringDeduplication""""
)
bashScriptExtraDefines ++= Seq(
  """addApp "-log.level=${LOG_LEVEL:-INFO}"""",
  """addApp "-swagger.docs.endpoint=${SWAGGER_DOC_PATH:-/practice/docs}"""",
  s"""addApp "-service.version=${version.value}""""
)

val gitHeadCode = SettingKey[String]("git-head-hash", "The commit hash code of HEAD")
gitHeadCode := git.gitHeadCommit.value
  .map { sha =>
    s"${sha.take(7)}"
  }
  .getOrElse("na")

dockerVersion := Some(DockerVersion(17, 9, 1, Some("ce")))
defaultLinuxInstallLocation in Docker := "/opt/finatra-sample-service"
packageName in Docker := "vr/finatra-sample-service"
// dockerBaseImage := "openjdk:8-jre-slim"
dockerBaseImage := "findepi/graalvm:1.0.0-rc10"
version in Docker := s"${if (gitHeadCode.value != "na") s"${version.value}_${gitHeadCode.value}" else version.value}"
maintainer in Docker := "Richard Chuo <richard_chuo@htc.com>"
dockerExposedPorts := Seq(9999, 9990)
dockerAlias := DockerAlias(None, None, (packageName in Docker).value, Some((version in Docker).value))
dockerUpdateLatest := false
dockerBuildOptions := Seq(
  "--force-rm",
  "-t",
  s"${(packageName in Docker).value}:${(version in Docker).value}",
  "--no-cache",
  "--pull"
)
dockerCommands := dockerCommands.value.take(1) ++ Seq(
  Cmd("LABEL", s"version=${version.value}"),
  Cmd("LABEL", "owner_team=MyTeam"),
  Cmd("LABEL", s"""build_id=${Option(System.getProperty("build_id")).getOrElse("NA")}"""),
  Cmd("ENV", "DOCKER_CONTENT_TRUST=1", "SERVICE_NAME=finatra-sample-service SERVICE_TAGS=vr,service")
) ++ dockerCommands.value.drop(1)

// AWS ECR support
region in Ecr := Region.getRegion(Regions.US_WEST_2)
repositoryName in Ecr := (packageName in Docker).value
localDockerImage in Ecr := (packageName in Docker).value + ":" + (version in Docker).value
repositoryTags in Ecr := Seq((version in Docker).value)
push in Ecr := ((push in Ecr) dependsOn (publishLocal in Docker, createRepository in Ecr, login in Ecr)).value
publish in Docker := (push in Ecr).value

// MicroSites
micrositeName := "practice"
micrositeBaseUrl := "a-sample-service"
micrositeDocumentationUrl := "/a-sample-service/docs"
micrositeAuthor := "Peter Parker"
micrositeOrganizationHomepage := "http://www.viveduric.com"
micrositeGitHostingService := Other("HICHub")
micrositeGitHostingUrl := "http://www.viveduric.com"

// License report style
licenseReportStyleRules := Some("table, th, td {border: 1px solid grey;}")
