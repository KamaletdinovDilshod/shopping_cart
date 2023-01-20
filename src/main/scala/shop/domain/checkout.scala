package shop.domain

import derevo.cats.show
import derevo.circe.magnolia._
import derevo.derive
import eu.timepit.refined.api.{Refined, Validate}
import eu.timepit.refined.boolean.And
import eu.timepit.refined.collection.Size
import eu.timepit.refined.string._
import io.estatico.newtype.macros.newtype
import io.circe.refined._

object checkout {

  type Rgx                = "^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$"
  type CardNamePred       = String Refined MatchesRegex[Rgx]
  type CardNumberPred     = Long Refined Size[16]
  type CardExpirationPred = String Refined (Size[4] And ValidInt)
  type CardCVVPred        = Int Refined Size[3]

  @derive(decoder, encoder, show)
  @newtype case class CardName(value: CardNamePred)

  @derive(encoder, show)
  @newtype case class CardNumber(value: CardNumberPred)

  @derive(encoder, show)
  @newtype case class CardExpiration(value: CardExpirationPred)

  @derive(encoder, show)
  @newtype case class CardCVV(value: CardCVVPred)

  @derive(decoder, encoder, show)
  case class Card(
      name: CardName,
      number: CardNumber,
      expiration: CardExpiration,
      ccv: CardCVV
  )

}
