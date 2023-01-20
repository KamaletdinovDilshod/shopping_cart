package shop.domain

import derevo.cats.{ eqv, show }
import derevo.circe.magnolia.{ decoder, encoder }
import derevo.derive
import io.estatico.newtype.macros.newtype
import shop.optics.uuid
import java.util.UUID
import scala.util.control.NoStackTrace

object auth {

  @derive(decoder, encoder, show)
  @newtype case class UserName(value: String)

  @derive(decoder, encoder)
  @newtype case class Password(value: String)

  @derive(show)
  @newtype case class EncryptedPassword(value: String)

  @derive(decoder, encoder, eqv, show, uuid)
  @newtype case class UserId(value: UUID)

  @derive(decoder, encoder, eqv, show)
  @newtype case class JwtToken(value: String)

  case class UserNameInUse(value: UserName)   extends NoStackTrace
  case class UserNotFound(username: UserName) extends NoStackTrace
  case class InvalidPassword(username: UserName) extends NoStackTrace

}
