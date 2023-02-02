package shop.http.routes

import cats.effect.IO
import io.circe.generic.auto.exportEncoder
import org.http4s.Method.GET
import org.http4s.client.dsl.io._
import org.http4s.implicits._
import org.scalacheck.Gen
import shop.generators.brandGen
import org.http4s._
import shop.domain.ID
import shop.domain.brand.{Brand, BrandId, BrandName}
import shop.services.Brands
import shop.suite.HttpSuite

object BrandRoutesSuite extends HttpSuite {

  def dataBrands(brands: List[Brand]) =
    new TestBrands {
      override def findAll: IO[List[Brand]] = IO.pure(brands)
    }

  test("GET brands succeeds") {
    forall(Gen.listOf(brandGen)) { b =>
      val req    = GET(uri"/brands")
      val routes = BrandRoutes[IO](dataBrands(b)).routes
      expectHttpBodyAndStatus(routes, req)(b, Status.Ok)
    }
  }
}

protected class TestBrands extends Brands[IO] {
  def create(name: BrandName): IO[BrandId] = ID.make[IO, BrandId]

  def findAll: IO[List[Brand]] = IO.pure(List.empty)
}
