package shop.domain

import cats.implicits._
import io.circe.{ Decoder, Encoder }
import squants.market.{ Currency, Money, USD }
import dev.profunktor.auth.jwt.JwtToken
import cats._

package object domain extends OrphanInstances {}

trait OrphanInstances {
  implicit val moneyDecoder: Decoder[Money] = Decoder[BigDecimal].map(USD.apply)

  implicit val moneyEncoder: Encoder[Money] = Encoder[BigDecimal].contramap(_.amount)

  implicit val currencyEq: Eq[Currency] = Eq.and(Eq.and(Eq.by(_.code), Eq.by(_.symbol)), Eq.by(_.name))

  implicit val moneyEq: Eq[Money] = Eq.and(Eq.by(_.amount), Eq.by(_.currency))

  implicit val tokenEq: Eq[JwtToken] = Eq.by(_.value)

  implicit val tokenShow: Show[JwtToken] = Show[String].contramap[JwtToken](_.value)

  implicit val tokenEncoder: Encoder[JwtToken] = Encoder.forProduct1("access_token")(_.value)

  implicit val moneyMonoid: Monoid[Money] =
    new Monoid[Money] {
      def empty: Money                       = USD(0)
      def combine(x: Money, y: Money): Money = x + y
    }
}
