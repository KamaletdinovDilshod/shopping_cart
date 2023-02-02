package shop.domain

import shop.domain.items._
import derevo.cats._
import derevo.circe.magnolia.{decoder, encoder}
import derevo.derive
import io.estatico.newtype.macros.newtype
import shop.domain.auth.UserId
import shop.optics.uuid
import squants.market.{Money, USD}
import shop.domain.domain._

import java.util.UUID
import scala.util.control.NoStackTrace
object cart {

  @derive(decoder, encoder, eqv, show)
    @newtype case class Quantity(value: Int)

  @derive(decoder, encoder, eqv, show)
  @newtype case class Cart(items: Map[ItemId, Quantity])

  @derive(decoder, encoder, eqv, show, uuid)
  @newtype case class CartId(value: UUID)

  @derive(decoder, encoder, eqv, show)
  case class CartItem(item: Item, quantity: Quantity) {
    def subTotal: Money = USD(item.price.amount * quantity.value)
  }
  @derive(decoder, encoder, eqv, show)
  case class CartTotal(items: List[CartItem], total: Money)

  @derive(decoder, encoder)
  case class CartNotFound(userId: UserId) extends NoStackTrace
}
