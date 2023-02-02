package shop.http.routes.secured

import shop.http.authentication.users.CommonUser
import shop.services._
import shop.domain.cart.{ Cart, CartTotal, Quantity }
import shop.domain.items.ItemId
import squants.market.USD
import shop.generators.{ cartGen, cartTotalGen, commonUserGen }
import shop.domain.auth.UserId
import cats.data.Kleisli
import cats.effect._
import io.circe.generic.auto.exportEncoder
import org.http4s.Method._
import org.http4s._
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.client.dsl.io._
import org.http4s.server.AuthMiddleware
import org.http4s.syntax.literals._
import shop.suite.HttpSuite

object CartRoutesSuite extends HttpSuite {

  def authMiddleware(authUser: CommonUser): AuthMiddleware[IO, CommonUser] = AuthMiddleware(Kleisli.pure(authUser))
  def dataCart(total: CartTotal) = new TestShoppingCart {
    override def get(userId: UserId): IO[CartTotal] = IO.pure(total)
  }

  test("GET shopping cart succeeds") {
    val gen = for {
      u <- commonUserGen
      c <- cartTotalGen
    } yield u -> c

    forall(gen) {
      case (user, ct) =>
        val req    = GET(uri"/cart")
        val routes = CartRoutes[IO](dataCart(ct)).routes(authMiddleware(user))
        expectHttpBodyAndStatus(routes, req)(ct, Status.Ok)
    }
  }

  test("POST add item to shopping cart succeeds") {
    val gen = for {
      u <- commonUserGen
      c <- cartGen
    } yield u -> c

    forall(gen) {
      case (user, c) =>
        val req = POST(c, uri"/cart")
        val routes = {
          CartRoutes[IO](new TestShoppingCart)
            .routes(authMiddleware(user))
          expectHttpStatus(routes, req)(Status.Created)
        }
    }
  }
}

class TestShoppingCart extends ShoppingCart[IO] {
  def add(
      userId: UserId,
      itemId: ItemId,
      quantity: Quantity
  ): IO[Unit] = IO.unit

  def get(userId: UserId): IO[CartTotal] = IO.pure(CartTotal(List.empty, USD(0)))

  def delete(userId: UserId): IO[Unit] = IO.unit

  def removeItem(userId: UserId, itemId: ItemId): IO[Unit] = IO.unit

  def update(userId: UserId, cart: Cart): IO[Unit] = IO.unit
}
