package shop.auth

import shop.domain.auth.JwtToken

trait Tokens[F[_]] {
  def create: F[JwtToken]
}
