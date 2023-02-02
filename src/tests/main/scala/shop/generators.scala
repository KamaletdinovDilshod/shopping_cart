package shop

import shop.domain.brand._
import shop.domain.items._
import shop.domain.cart._
import shop.domain.category._
import shop.domain.checkout._
import shop.http.authentication.users._
import org.scalacheck.Gen
import eu.timepit.refined.api.Refined
import shop.domain.auth.{EncryptedPassword, Password, UserId, UserName}
import shop.domain.order.{OrderId, PaymentId}
import shop.domain.payment.Payment
import squants.market._

import java.util.UUID

object generators {

  val nonEmptyStringGen: Gen[String] =
    Gen
      .chooseNum(21, 40)
      .flatMap { n =>
        Gen.buildableOfN[String, Char](n, Gen.alphaChar)
      }

  def nesGen[A](f: String => A): Gen[A] = nonEmptyStringGen.map(f)
  def idGen[A](f: UUID => A): Gen[A]    = Gen.uuid.map(f)

  val brandIdGen: Gen[BrandId]     = idGen(BrandId.apply)
  val brandNameGen: Gen[BrandName] = nesGen(BrandName.apply)

  val categoryIdGen: Gen[CategoryId]     = idGen(CategoryId.apply)
  val categoriNameGen: Gen[CategoryName] = nesGen(CategoryName.apply)

  val moneyGen: Gen[Money] = Gen.posNum[Long].map(n => USD(BigDecimal(n)))

  val itemIdGen: Gen[ItemId]                   = idGen(ItemId.apply)
  val itemNameGen: Gen[ItemName]               = nesGen(ItemName.apply)
  val itemDescriptionGen: Gen[ItemDescription] = nesGen(ItemDescription.apply)

  val quantityGen: Gen[Quantity] = Gen.posNum[Int].map(Quantity.apply)

  val userIdGen: Gen[UserId]    = idGen(UserId.apply)
  val useNameGen: Gen[UserName] = nesGen(UserName.apply)

  val paymentIdGen: Gen[PaymentId] = idGen(PaymentId.apply)
  val paymentGen: Gen[Payment] =
    for {
      i <- userIdGen
      m <- moneyGen
      c <- cardGen
    } yield Payment(i, m, c)

  val orderIdGen: Gen[OrderId] = idGen(OrderId.apply)

  val brandGen: Gen[Brand] =
    for {
      i <- brandIdGen
      n <- brandNameGen
    } yield Brand(i, n)

  val categoryGen: Gen[Category] =
    for {
      i <- categoryIdGen
      n <- categoriNameGen
    } yield Category(i, n)

  val itemGen: Gen[Item] =
    for {
      i <- itemIdGen
      n <- itemNameGen
      d <- itemDescriptionGen
      p <- moneyGen
      b <- brandGen
      c <- categoryGen
    } yield Item(i, n, d, p, b, c)

  val cartItemGen: Gen[CartItem] =
    for {
      i <- itemGen
      q <- quantityGen
    } yield CartItem(i, q)

  val cartTotalGen: Gen[CartTotal] =
    for {
      i <- Gen.nonEmptyListOf(cartItemGen)
      t <- moneyGen
    } yield CartTotal(i, t)

  val itemMapGen: Gen[(ItemId, Quantity)] =
    for {
      i <- itemIdGen
      q <- quantityGen
    } yield i -> q

  val cartGen: Gen[Cart] = Gen.nonEmptyMap(itemMapGen).map(Cart.apply)

  val cardNameGen: Gen[CardName] =
    Gen
      .stringOf(
        Gen.oneOf(('a' to 'z') ++ ('A' to 'Z'))
      )
      .map { x =>
        CardName(Refined.unsafeApply(x))
      }

  private def sized(size: Int): Gen[Long] = ???

  val cardGen: Gen[Card] =
    for {
      n <- cardNameGen
      u <- sized(16).map(x => CardNumber(Refined.unsafeApply(x)))
      x <- sized(4).map(x => CardExpiration(Refined.unsafeApply(x.toString)))
      c <- sized(3).map(x => CardCVV(Refined.unsafeApply(x.toInt)))
    } yield Card(n, u, x, c)

  val userGen: Gen[User] =
    for {
      i <- userIdGen
      n <- useNameGen
    } yield User(i, n)

  val commonUserGen: Gen[CommonUser] = userGen.map(v => CommonUser(v))

  val userNameGen: Gen[UserName] = nesGen(UserName.apply)

  val encryptedPasswordGen: Gen[EncryptedPassword] = nesGen(EncryptedPassword.apply)

  val passwordGen: Gen[Password] = nesGen(Password.apply)


}
