package shop.config

import ciris.Secret
import derevo.cats.show
import derevo.derive
import eu.timepit.refined.types.net.UserPortNumber
import eu.timepit.refined.types.numeric.PosInt
import eu.timepit.refined.types.string.NonEmptyString
import io.estatico.newtype.macros.newtype
import shop.ext.ciris.configDecoder
import com.comcast.ip4s.{ Host, Port }
import ciris.refined._ // Don't remove
import eu.timepit.refined.cats._ // Don't remove

import scala.concurrent.duration.FiniteDuration

object types {

  @newtype case class ShoppingCartExpiration(value: FiniteDuration)

  @derive(configDecoder, show)
  @newtype case class AdminUserTokenConfig(secret: NonEmptyString)

  @derive(configDecoder, show)
  @newtype case class JwtClaimConfig(secret: NonEmptyString)

  @newtype case class TokenExpiration(value: FiniteDuration)

  @newtype case class PaymentURI(value: NonEmptyString)

  @newtype case class PaymentConfig(uri: PaymentURI)

  @newtype case class RedisURI(value: NonEmptyString)

  @newtype case class RedisConfig(uri: RedisURI)

  case class AdminJwtConfig(
      secretKey: Secret[JwtSecretKeyConfig],
      claimStr: Secret[JwtClaimConfig],
      adminToken: Secret[AdminUserTokenConfig]
  )

  case class CheckoutConfig(
      retriesLimit: PosInt,
      retriesBackoff: FiniteDuration
  )

  case class AppConfig(
      adminJwtConfig: AdminJwtConfig,
      tokenConfig: Secret[JwtAccessTokenKeyConfig],
      passwordSalt: Secret[PasswordSalt],
      tokenExpiration: TokenExpiration,
      cartExpiration: ShoppingCartExpiration,
      checkoutConfig: CheckoutConfig,
      paymentConfig: PaymentConfig,
      httpClientConfig: HttpClientConfig,
      postgreSQL: PostgresSQLConfig,
      redis: RedisConfig,
      httpServerConfig: HttpServerConfig
  )

  case class PostgresSQLConfig(
      host: NonEmptyString,
      port: UserPortNumber,
      user: NonEmptyString,
      password: Secret[NonEmptyString],
      database: NonEmptyString,
      max: PosInt
  )

  case class HttpServerConfig(
      host: Host,
      port: Port
  )

  @derive(configDecoder, show)
  @newtype
  case class JwtSecretKeyConfig(secret: NonEmptyString)

  @derive(configDecoder, show)
  @newtype
  case class JwtAccessTokenKeyConfig(secret: NonEmptyString)

  @derive(configDecoder, show)
  @newtype
  case class PasswordSalt(secret: NonEmptyString)

  case class HttpClientConfig(
      timeout: FiniteDuration,
      idleTimeInPool: FiniteDuration
  )

}
