package shop.domain

import org.scalacheck.Arbitrary
import weaver.FunSuite
import shop.generators.moneyGen
import squants.market.Money
import weaver.discipline.Discipline
import cats.kernel.laws.discipline.MonoidTests
import shop.domain.domain.{moneyEq, moneyMonoid}

object OrphanSuite extends FunSuite with Discipline {
  implicit val arbMoney: Arbitrary[Money] = Arbitrary(moneyGen)

  checkAll("Monoid[Money]", MonoidTests[Money].monoid)
}
