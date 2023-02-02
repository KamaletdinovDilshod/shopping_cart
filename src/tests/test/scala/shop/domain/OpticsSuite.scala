package shop.domain

import org.scalacheck.{Arbitrary, Cogen, Gen}
import shop.domain.brand.BrandId
import shop.generators.brandIdGen
import weaver.FunSuite
import weaver.discipline.Discipline
import shop.domain.healthcheck.Status
import shop.optics.IsUUID
import shop.domain.category.CategoryId
import shop.sql.codecs.categoryId
import monocle.law.discipline.IsoTests

import java.util.UUID

object OpticsSuite extends FunSuite with Discipline {

  implicit val arbStatus: Arbitrary[Status] =
    Arbitrary(Gen.oneOf(Status.Okay, Status.Unreachable))

  implicit val uuidCogen: Cogen[UUID] =
    Cogen[(Long, Long)].contramap { uuid =>
      uuid.getLeastSignificantBits -> uuid.getMostSignificantBits
    }

  implicit val brandIdArb: Arbitrary[BrandId] =
    Arbitrary(brandIdGen)

  implicit val brandIdCogen: Cogen[BrandId] =
    Cogen[UUID].contramap[BrandId](_.value)

  implicit val brandIdCogen: Cogen[BrandId] =
    Cogen[UUID].contramap[BrandId](_.value)

  implicit val catIdArb: Arbitrary[CategoryId] =
    Arbitrary(categoryId)

  implicit val catIdCogen: Cogen[CategoryId] =
    Cogen[UUID].contramap[CategoryId](_.value)

  checkAll("Iso[Status._Bool]", IsoTests(Status._Bool))


  checkAll("IsUUID[BrandId]", IsoTests(IsUUID[BrandId]._UUID))
  checkAll("IsUUID[Status._Bool]", IsoTests(Status._Bool))
  checkAll("IsUUID[UUID]", IsoTests(IsUUID[UUID]._UUID))
}
