package shop.http.routes.auth

import shop.services.Auth
import shop.ext.http4s.refined._
import shop.domain.auth._
import shop.domain._

import cats.MonadThrow
import org.http4s.HttpRoutes
import org.http4s.circe.JsonDecoder
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router
import org.http4s.circe.CirceEntityEncoder._


final case class UserRoutes[F[_]: JsonDecoder: MonadThrow](auth: Auth[F]) extends Http4sDsl[F] {

  private[routes] val prefixPath = "/auth"

  private val httpRoutes: HttpRoutes[F] = HttpRoutes.of[F] {
    case req @ POST -> Root / "users" =>
      req
      .decodeR[CreateUser] { user =>
        auth
          .newUser(
            user.username.toDomain,
            user.username.toDomain
          )
          .flatMap(Created(_))
          .recoverWith {
            case UserNameInUse(u) =>
              Conflict(u.show)
          }
      }
  }
  val routes: HttpRoutes[F] = Router(
    prefixPath -> httpRoutes
  )

}
