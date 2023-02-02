package shop.http.routes.admin

import shop.domain.items._
import shop.services.Items
import shop.ext.http4s.refined.RefinedRequestDecoder
import cats.MonadThrow
import cats.implicits._
import org.http4s._
import org.http4s.circe.JsonDecoder
import org.http4s.dsl.Http4sDsl
import org.http4s.server._
import org.http4s.circe.CirceEntityEncoder._
import io.circe.JsonObject
import io.circe.generic.auto._
import io.circe.syntax.EncoderOps
import shop.http.authentication.users.AdminUser

final case class AdminItemRoutes[F[_]: JsonDecoder: MonadThrow](
    items: Items[F]
) extends Http4sDsl[F] {
  private[admin] val prefixPath = "/items"

  private val httpRoutes: AuthedRoutes[AdminUser, F] =
    AuthedRoutes.of {
      //Create new item
      case ar @ POST -> Root as _ =>
        ar.req.decodeR[CreateItemParam] { item =>
          items.create(item.toDomain).flatMap { id =>
            Created(JsonObject.singleton("item_id", id.asJson))
          }
        }

      //Update price of item
      case ar @ PUT -> Root as _ =>
        ar.req.decodeR[UpdateItemParam] { item =>
          items.update(item.toDomain) >> Ok()
        }
    }

  def routes(
      authMiddleware: AuthMiddleware[F, AdminUser]
  ): HttpRoutes[F] = Router(
    prefixPath -> authMiddleware(httpRoutes)
  )
}
