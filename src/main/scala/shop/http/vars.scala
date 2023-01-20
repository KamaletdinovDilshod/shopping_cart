package shop.http

import java.util.UUID
import shop.domain.items.ItemId
import cats.implicits._
import shop.domain.order.OrderId


object vars {

  object ItemIdVar{
    def unapply(str: String): Option[ItemId] =
      Either.catchNonFatal(ItemId(UUID.fromString(str))).toOption
  }

  object OrderIdVar {
    def unapply(str: String): Option[OrderId] =
      Either.catchNonFatal(OrderId(UUID.fromString(str))).toOption
  }
}
