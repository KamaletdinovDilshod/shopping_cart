package shop.domain

import shop.domain.auth.UserId
import shop.domain.checkout.Card
import derevo.circe.magnolia.{decoder, encoder}
import derevo.derive
import squants.market.Money
import eu.timepit.refined.cats._ //Don't remove
import shop.domain.domain._  // Don't remove

object payment {
  @derive(decoder, encoder)
  case class Payment(
      id: UserId,
      total: Money,
      card: Card
  )
}
