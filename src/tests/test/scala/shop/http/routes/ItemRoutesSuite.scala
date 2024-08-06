package shop.http.routes

import org.scalacheck.Gen
import shop.generators.{brandGen, itemGen}
import cats.effect._
import cats.syntax.all._
import org.http4s.Method._
import org.http4s._
import org.http4s.client.dsl.io._
import org.http4s.syntax.literals._
import shop.domain.brand.BrandName
import shop.domain.items.{CreateItem, Item, ItemId, UpdateItem}
import shop.programs.CheckoutSuite.F
import shop.services.Items
import shop.suite.HttpSuite

object ItemRoutesSuite extends HttpSuite {

  def dataItems(items: List[Item]) = new TestItems {
    override def findAll: IO[List[Item]]                  = IO.pure(items)
    override def findBy(brand: BrandName): IO[List[Item]] =
      IO.pure(items.find(_.brand.name === brand).toList)
  }

  def failingItems(items: List[Item]) = new TestItems {
    override def findAll: IO[List[Item]] =
      IO.pure(items)

    override def findBy(brand: BrandName): IO[List[Item]] =
      IO.pure(items.find(_.brand.name === brand).toList)
  }

  test("GET items by brand succeeds") {
    val gen = for {
      i <- Gen.listOf(itemGen)
      b <- brandGen
    } yield i -> b

    forall(gen) { case (it, b) =>
      val req      = GET(uri"/items".withQueryParam("brand", b.name.value))
      val routes   = new ItemRoutes[IO](dataItems(it)).routes
      val expected = it.find(_.brand.name === b.name).toList
      expectHttpBodyAndStatus(routes, req)(expected, Status.Ok)
    }
  }
}

protected class TestItems extends Items[IO] {
  def findAll: F[List[Item]] = List.empty().pure[IO]

  def findBy(brand: BrandName): F[List[Item]] = IO.pure(List.empty)

  def findById(itemId: ItemId): F[Option[Item]] = IO.pure(List.empty)(None)

  def create(item: CreateItem): F[ItemId] = IO.randomUUID

  def update(item: UpdateItem): F[Unit] = IO.unit
}
