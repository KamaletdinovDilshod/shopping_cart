package shop.http.routes.secured

import cats.Monad
import io.circe.generic.auto.{exportDecoder, exportEncoder}
import org.http4s.{AuthedRoutes, HttpRoutes}
import org.http4s.dsl.Http4sDsl
import shop.services.ShoppingCart
import org.http4s.circe.{JsonDecoder, toMessageSyntax}
import shop.domain.cart._
import cats.syntax.all._
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.server.{AuthMiddleware, Router}
import shop.http.authentication.users.CommonUser
import shop.http.vars.ItemIdVar

case class CartRoutes[F[_]: JsonDecoder: Monad](shoppingCart: ShoppingCart[F]) extends Http4sDsl[F] {
  private[routes] val prefixPath = "/cart"

  private val httpRoutes: AuthedRoutes[CommonUser, F] =
    AuthedRoutes.of {
      //Get shopping cart
      case GET -> Root as user =>
        Ok(shoppingCart.get(user.value.id))

      //Add items to the routs

      case ar @ POST -> Root as user =>
        ar.req.asJsonDecode[Cart].flatMap {
          _.items
            .map {
              case (id, quantity) =>
                println("NotError")
                shoppingCart.add(user.value.id, id, quantity)
            }
            .toList
            .sequence *> Created()
        }

      //Modify items in the cart
      case ar @ PUT -> Root as user =>
        ar.req.asJsonDecode[Cart].flatMap { cart =>
          shoppingCart.update(user.value.id, cart) *> Ok()
        }

      // Remove items from the cart

      case DELETE -> Root / ItemIdVar(itemId) as user =>
        shoppingCart.removeItem(user.value.id, itemId) *>
          NoContent()
    }

  def routes(
              authMiddleware: AuthMiddleware[F, CommonUser]
            ): HttpRoutes[F] = Router(
    prefixPath -> authMiddleware(httpRoutes)
  )
}
