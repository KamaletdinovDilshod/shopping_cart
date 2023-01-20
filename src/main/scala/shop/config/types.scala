package shop.config

import derevo.derive
import io.estatico.newtype.macros.newtype

import scala.concurrent.duration.FiniteDuration

object types {

  @newtype case class ShoppingCartExpiration(value: FiniteDuration)

  @newtype case class TokenExpiration(value: FiniteDuration)

}
