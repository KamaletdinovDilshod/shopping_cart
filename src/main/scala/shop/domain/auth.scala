package shop.domain

import cats.{Eq, Show}
import cats.implicits.toContravariantOps

import java.util.UUID
import scala.util.control.NoStackTrace
import shop.optics.uuid
import derevo.cats._
import derevo.circe.magnolia.{decoder, encoder}
import derevo.derive
import eu.timepit.refined.auto._
import eu.timepit.refined.cats.refTypeEq
import eu.timepit.refined.types.string.NonEmptyString
import io.circe._
import io.circe.refined._
import io.estatico.newtype.macros.newtype

import javax.crypto.Cipher

object auth {

  @derive(decoder, encoder, eqv, show)
  @newtype case class UserName(value: String)

  @derive(decoder, encoder, eqv, show)
  @newtype case class Password(value: String)

  @derive(decoder, encoder, eqv, show)
  @newtype case class EncryptedPassword(value: String)

  @newtype
  case class EncryptCipher(value: Cipher)

  @newtype
  case class DecryptCipher(value: Cipher)

  @derive(decoder, encoder, eqv, show, uuid)
  @newtype case class UserId(value: UUID)

  @newtype case class ClaimContent(uuid: UUID)

  object ClaimContent {
    implicit val jsonDecoder: Decoder[ClaimContent] =
      Decoder.forProduct1("uuid")(ClaimContent.apply)
  }

  @newtype case class UserNameParam(value: NonEmptyString) {
    def toDomain: UserName = UserName(value.toLowerCase)
  }

  object UserNameParam {
    implicit val encoderUserNameParam: Decoder[UserNameParam] =
      Decoder.forProduct1("username")(UserNameParam.apply)

    implicit val decodeUserNameParam: Encoder[UserNameParam] =
      Encoder.forProduct1("username")(_.value)

    implicit val userNameEq: Eq[UserNameParam] = Eq.by(_.value)

    implicit val userNameShow: Show[UserNameParam] = Show[String].contramap[UserNameParam](_.value)
  }

  @newtype case class PasswordParam(value: NonEmptyString) {
    def toDomain: Password = Password(value)
  }

  object PasswordParam{
    implicit val encoderPasswordParam: Decoder[PasswordParam] =
      Decoder.forProduct1("password")(PasswordParam.apply)

    implicit val decodePasswordParam: Encoder[PasswordParam] =
      Encoder.forProduct1("password")(_.value)

    implicit val passwordParamEq: Eq[PasswordParam] = Eq.by(_.value)

    implicit val passwordParam: Show[PasswordParam] = Show[String].contramap[PasswordParam](_.value)
  }

  @derive(decoder, encoder)
  case class CreateUser(username: UserNameParam, password: PasswordParam)

  @derive(decoder, encoder)
  case class LoginUser(
      username: UserNameParam,
      password: PasswordParam
  )

  case class UserNameInUse(value: UserName)      extends NoStackTrace
  case class UserNotFound(username: UserName)    extends NoStackTrace
  case class InvalidPassword(username: UserName) extends NoStackTrace
}
