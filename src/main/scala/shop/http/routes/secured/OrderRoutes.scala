package shop.http.routes.secured

import cats.{Monad, Order}
import io.circe.generic.auto.exportEncoder
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.{AuthedRoutes, HttpRoutes}
import org.http4s.dsl.Http4sDsl
import org.http4s.server.{AuthMiddleware, Router}
import shop.http.authentication.users.CommonUser
import shop.services.Orders
import shop.http.vars.OrderIdVar

case class OrderRoutes[F[_]: Monad](orders: Orders[F]) extends Http4sDsl[F] {
  private[routes] val prefixPath = "/orders"
  private val httpRoutes: AuthedRoutes[CommonUser, F] = AuthedRoutes.of {
    case GET -> Root as user =>
      Ok(orders.findBy(user.value.id))

    case GET -> Root / OrderIdVar(orderId) as user =>
      Ok(orders.get(user.value.id, orderId))
  }

  def routes(authMiddleware: AuthMiddleware[F, CommonUser]): HttpRoutes[F] = Router(
    prefixPath -> authMiddleware(httpRoutes)
  )


}
