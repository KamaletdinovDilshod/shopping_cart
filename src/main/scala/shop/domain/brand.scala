package shop.domain

import shop.optics.uuid

import derevo.cats._
import derevo.circe.magnolia.{decoder, encoder}
import derevo.derive
import eu.timepit.refined.auto._
import eu.timepit.refined.cats._
import eu.timepit.refined.types.string.NonEmptyString
import io.circe.refined._
import io.circe.{Decoder, Encoder}
import io.estatico.newtype.macros.newtype
import shop.ext.http4s.queryParam
import shop.optics.uuid




import java.util.UUID

object brand {

  @derive(decoder, encoder, eqv, show, uuid)
  @newtype case class BrandId(value: UUID)

  @derive(decoder, encoder, eqv, show)
  @newtype case class BrandName(value: String)

  case class Brand(uuid: BrandId, name: BrandName)

  @derive(queryParam, show)
  @newtype case class BrandParam(value: NonEmptyString) {
    def toDomain: BrandName =
      BrandName(value.toLowerCase.capitalize)
  }
}
