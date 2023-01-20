package shop.domain

import derevo.cats._
import derevo.circe.magnolia.{decoder, encoder}
import derevo.derive
import eu.timepit.refined.auto._
import eu.timepit.refined.types.string.NonEmptyString
import io.circe.Decoder
import io.circe.refined._
import io.estatico.newtype.macros.newtype
import shop.ext.http4s.queryParam

import java.util.UUID

object category {
  @newtype case class CategoryId(value: UUID)

  @newtype case class CategoryName(value: String)

  case class Category(uuid: CategoryId, name: CategoryName)

  @derive(queryParam, show)
  @newtype case class CategoryParam(value: NonEmptyString) {
    def toDomain: CategoryName =
      CategoryName(value.toLowerCase.capitalize)
  }

}
