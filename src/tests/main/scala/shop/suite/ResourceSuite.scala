package shop.suite

import cats.effect.{IO, Resource}
import cats.implicits.toFlatMapOps
import weaver.IOSuite
import weaver.scalacheck.{CheckConfig, Checkers}
abstract class ResourceSuite extends IOSuite with Checkers {

  override def checkConfig: CheckConfig =
    CheckConfig.default.copy(minimumSuccessful = 1)

  implicit class SharedResOps(res: Resource[IO, Res]) {
    def beforeAll(f: Res => IO[Unit]): Resource[IO, Res] =
      res.evalTap(f)

    def afterAll(f: Res => IO[Unit]): Resource[IO, Res] =
      res.flatTap(x => Resource.make(IO.unit)(_ => f(x)))
  }
}
