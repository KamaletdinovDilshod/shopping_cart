ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

val circeVersion = "0.14.2"
val http4Version = "0.23.1"
lazy val doobieVersion = "1.0.0-RC1"

lazy val root = (project in file("."))
  .settings(
    name := "shopping_cart",
    libraryDependencies ++= Seq (
      compilerPlugin(
        "org.typelevel" %% "kind-projector"                 % "0.13.2"
          cross CrossVersion.full
      ),
      "org.typelevel"           %% "cats-core"                      % "2.9.0",
      "org.typelevel"           %% "cats-effect"                    % "3.4.4",
      "org.typelevel"           %% "cats-mtl"                       % "1.3.0",
      //Kittens
      "org.typelevel"           %% "kittens"                        % "2.1.0",
      //catnip
      "io.scalaland"            %% "catnip"                         % "1.1.2",

      "co.fs2"                  %% "fs2-core"                       % "3.4.0",
      "dev.optics"              %% "monocle-core"                   % "3.1.0",
      "dev.optics"              %% "monocle-macro"                  % "3.1.0",
      "io.estatico"             %% "newtype"                        % "0.4.4",
      "eu.timepit"              %% "refined"                        % "0.10.1",
      "eu.timepit"              %% "refined-cats"                   % "0.10.1",
      "tf.tofu"                 %% "derevo-cats"                    % "0.13.0",
      "tf.tofu"                 %% "derevo-cats-tagless"            % "0.13.0",
      "tf.tofu"                 %% "derevo-circe-magnolia"          % "0.13.0",
      "tf.tofu"                 %% "tofu-core-higher-kind"          % "0.11.1",
      "com.github.cb372"        %% "cats-retry"                     % "3.1.0",
      "is.cir"                  %% "ciris"                          % "3.0.0",
      "org.typelevel"           %% "log4cats-slf4j"                 % "2.5.0",
      //Squants
      "org.typelevel"           %% "squants"                        % "1.8.3",

      //http4s
      "org.http4s"              %% "http4s-ember-server"            % http4Version,
      "org.http4s"              %% "http4s-ember-client"            % http4Version,
      "org.http4s"              %% "http4s-dsl"                     % http4Version,
      "org.http4s"              %% "http4s-circe"                   % http4Version,

      //JWT auth
      "dev.profunktor"          %% "http4s-jwt-auth"                % "1.0.0",

      //Circe
      "io.circe"                %% "circe-core"                     % circeVersion,
      "io.circe"                %% "circe-generic"                  % circeVersion,
      "io.circe"                %% "circe-parser"                   % circeVersion,
      "io.circe"                %% "circe-refined"                  % circeVersion,

      //Shapeless
      "com.chuusai"             %% "shapeless"                      % "2.3.3",

      //Doobie
      "org.tpolecat"            %% "doobie-core"                    % doobieVersion,
      "org.tpolecat"            %% "doobie-postgres"                % doobieVersion,
      "org.tpolecat"            %% "doobie-specs2"                  % doobieVersion,

      //Skunk
      "org.tpolecat"            %% "skunk-core"                     % "0.3.2",
      "org.tpolecat"            %% "skunk-circe"                    % "0.3.2",

      //Redis
      "dev.profunktor"          %% "redis4cats-effects"             % "1.3.0",
      "dev.profunktor"          %% "redis4cats-log4cats"            % "1.3.0",

      // Weaver-Test
      "com.disneystreaming"     %% "weaver-cats"                    % "0.8.1" % Test,
      // optionally (for Scalacheck usage)
      "com.disneystreaming"     %% "weaver-scalacheck"              % "0.8.1" % Test,

      //scalacheck
      "org.typelevel"           %% "scalacheck-effect"              % "1.0.4",
      "org.typelevel"           %% "scalacheck-effect-munit"        % "1.0.4" % Test,


      //Munit
      "org.typelevel"           %% "munit-cats-effect-3"           % "1.0.7" % "test"


    ),
    scalacOptions ++= Seq(
      "-Ymacro-annotations", "-Wconf:cat=unused:info",
    ),

    testFrameworks += new TestFramework("weaver.framework.CatsEffect")


  )