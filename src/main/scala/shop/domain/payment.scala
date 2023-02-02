package shop.domain

import derevo.cats.show
import shop.domain.auth.UserId
import shop.domain.checkout.Card
import derevo.circe.magnolia.encoder
import derevo.derive
import squants.market.Money
import eu.timepit.refined.cats._
import shop.domain.domain._  // Don't remove

object payment {
  @derive(encoder, show)
  case class Payment(
      id: UserId,
      total: Money,
      card: Card
  )
}
