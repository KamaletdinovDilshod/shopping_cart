package shop.sql

import shop.domain.auth._
import shop.domain.brand._
import shop.domain.category._
import shop.domain.items._
import shop.domain.order.{OrderId, PaymentId}
import skunk._
import skunk.codec.all._
import squants.market._
object codecs {
  val brandId: Codec[BrandId]               = uuid.imap[BrandId](BrandId.apply)(_.value)
  val brandName: Codec[BrandName]           = varchar.imap[BrandName](BrandName.apply)(_.value)

  val categoryId: Codec[CategoryId]         = uuid.imap[CategoryId](CategoryId.apply)(_.value)
  val categoryName: Codec[CategoryName]     = varchar.imap[CategoryName](CategoryName.apply)(_.value)

  val itemId: Codec[ItemId]                 = uuid.imap[ItemId](ItemId.apply)(_.value)
  val itemName: Codec[ItemName]             = varchar.imap[ItemName](ItemName.apply)(_.value)
  val itemDesc: Codec[ItemDescription]      = varchar.imap[ItemDescription](ItemDescription.apply)(_.value)

  val money: Codec[Money]                   = numeric.imap[Money](USD(_))(_.amount)

  val orderId: Codec[OrderId]               = uuid.imap[OrderId](v => OrderId(v))(_.value)
  val paymentId: Codec[PaymentId]           = uuid.imap[PaymentId](v => PaymentId(v))(_.value)

  val userId: Codec[UserId]                 = uuid.imap[UserId](v => UserId(v))(_.value)
  val username: Codec[UserName]             = varchar.imap[UserName](v => UserName(v))(_.username)
  val encPassword: Codec[EncryptedPassword] = varchar.imap[EncryptedPassword](v => EncryptedPassword(v))(_.value)
}
