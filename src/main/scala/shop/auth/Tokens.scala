package shop.auth

import cats.Monad
import dev.profunktor.auth.jwt.JwtToken
import shop.config.types.{JwtAccessTokenKeyConfig, TokenExpiration}
import shop.effects.GenUUID

trait Tokens[F[_]] {
  def create: F[JwtToken]
}

object Tokens {
  def make[F[_]: GenUUID: Monad](
      jwtExpire: JwtExpire[F],
      config: JwtAccessTokenKeyConfig,
      exp: TokenExpiration
  ): Tokens[F] = ???

}
