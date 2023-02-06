package shop.http.routes.auth

import cats.MonadThrow
import cats.implicits._
import io.circe.generic.auto.exportEncoder
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.circe.JsonDecoder
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router
import shop.domain.auth.{CreateUser, UserNameInUse}
import shop.ext.http4s.refined._
import shop.services.Auth


final case class UserRoutes[F[_]: JsonDecoder: MonadThrow](auth: Auth[F]) extends Http4sDsl[F] {

  private[routes] val prefixPath = "/auth"

  private val httpRoutes: HttpRoutes[F] = HttpRoutes.of[F] {
    case req @ POST -> Root / "users" =>
      req
      .decodeR[CreateUser] { user =>
        auth
          .newUser(
            user.username.toDomain,
            user.password.toDomain
          )
          .flatMap(Created(_))
          .recoverWith {
            case UserNameInUse(u) =>
              Conflict(u.show)
            case er => println(s"EEEEEEE: $er")
              BadRequest("ISHLAMADI")
          }
      }
  }


  val routes: HttpRoutes[F] = Router(
    prefixPath -> httpRoutes
  )

}
