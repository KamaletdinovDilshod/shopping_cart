package shop.domain

import shop.optics.uuid
import shop.domain.items._
import derevo.cats._
import derevo.circe.magnolia.{ decoder, encoder }
import derevo.derive
import io.estatico.newtype.macros.newtype
import squants.market.{ Money, USD }

import java.util.UUID
object cart {

  @derive(decoder, encoder, eqv, show)
    @newtype case class Quantity(value: Int)

  @derive(eqv, show)
  @newtype case class Cart(items: Map[ItemId, Quantity])

  @derive(decoder, encoder, eqv, show, uuid)
  @newtype case class CartId(value: UUID)

  @derive(decoder, encoder, eqv, show)
  case class CartItem(item: Item, quantity: Quantity) {
    def subTotal: Money = USD(item.price.amount * quantity.value)
  }
  case class CartTotal(items: List[CartItem], total: Money)
}
