package shop.http.routes.secured

import cats.MonadThrow
import org.http4s.{AuthedRoutes, HttpRoutes}
import org.http4s.circe.JsonDecoder
import org.http4s.dsl.Http4sDsl
import org.http4s.server.{AuthMiddleware, Router}
import shop.programs.Checkout
import org.http4s.circe.CirceEntityEncoder._
import shop.http.authentication.users.CommonUser

import javax.smartcardio.Card

final case class CheckoutRoutes[F[_]: JsonDecoder: MonadThrow](checkout: Checkout[F]) extends Http4sDsl[F] {
  private[routes] val prefixPath = "/checkout"

  private val httpRoutes: AuthedRoutes[CommonUser, F] = {
    case ar @ POST -> Root as user =>
      ar.req.decodeR[Card] { card =>
        checkout
          .process(user.value.id, card)
          .flatMap(Created(_))
          .recoverWith {
            case CartNotFound(userId) =>
              NotFound(
                s"Cort not found for user: ${userId.value}"
              )
            case EmptyCartError =>
              BadRequest("Shopping cart is empty!")

            case e: OrderOrPaymentError =>
              BadRequest(e.show)
          }
      }
  }
  def routes(authMiddleware: AuthMiddleware[F, CommonUser]): HttpRoutes[F] = Router(
    prefixPath -> authMiddleware(httpRoutes)
  )
}
