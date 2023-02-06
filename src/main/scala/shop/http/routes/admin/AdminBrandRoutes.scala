package shop.http.routes.admin

import shop.ext.http4s.refined.RefinedRequestDecoder
import shop.services.Brands
import shop.domain.brand.BrandParam
import shop.http.authentication.users.AdminUser
import cats.MonadThrow
import cats.implicits.toFlatMapOps
import io.circe.generic.auto._
import io.circe.JsonObject
import io.circe.syntax.EncoderOps
import org.http4s.dsl.Http4sDsl
import org.http4s._
import org.http4s.server._
import org.http4s.circe.JsonDecoder
import org.http4s.circe.CirceEntityEncoder._

final case class AdminBrandRoutes[F[_]: JsonDecoder: MonadThrow](
    brands: Brands[F]
) extends Http4sDsl[F] {
  private[admin] val prefixPath = "/brands"

  private val httpRoutes: AuthedRoutes[AdminUser, F] = {
    AuthedRoutes.of {
      case ar @ POST -> Root as _ =>
        ar.req.decodeR[BrandParam] { bp =>
          brands.create(bp.toDomain).flatMap { id =>
            Created(JsonObject.singleton("brand_id", id.asJson))
          }
        }
    }
  }
  def routes(authMiddleware: AuthMiddleware[F, AdminUser]): HttpRoutes[F] = Router(
    prefixPath -> authMiddleware(httpRoutes)
  )
}
