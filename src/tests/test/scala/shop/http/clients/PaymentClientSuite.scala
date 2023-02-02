package shop.http.clients

import cats.effect.IO
import org.http4s.Method.POST
import org.http4s.Status.{Conflict, Ok}
import org.http4s.{HttpRoutes, Response}
import shapeless.ops.zipper.Root
import weaver.SimpleIOSuite
import weaver.scalacheck.Checkers
import org.http4s.client.Client
import org.http4s.dsl.io.InternalServerError
import shop.config.types.{PaymentConfig, PaymentURI}
import shop.domain.order.PaymentError
import shop.generators.{paymentGen, paymentIdGen}
import eu.timepit.refined.auto._
object PaymentClientSuite extends SimpleIOSuite with Checkers {

  val config = PaymentConfig(PaymentURI("http://localhost:"))

  def routes(mkResponse: IO[Response[IO]]) =
    HttpRoutes
      .of[IO] {
        case POST -> Root / "payments" => mkResponse
      }
      .orNotFound

  val gen = for {
    i <- paymentGen
    p <- paymentGen
  } yield i -> p

  test("Internal Server Error response (500)") {
    forall(paymentGen) { payment =>
      val client = Client.fromHttpApp(routes(InternalServerError()))

      PaymentClient
        .make[IO](config, client)
        .process(payment)
        .attempt
        .map {
          case Left(e)  => expect.same(PaymentError("Internal Server Error"), e)
          case Right(_) => failure("expected payment error")
        }
    }
  }

  test("Response Ok (200)") {
    forall(gen) {
      case (pid, payment) =>
        val client = Client.fromHttpApp(routes(Ok(pid)))

        PaymentClient
          .make[IO](config, client)
          .process(payment)
          .map(expect.same(pid, _))
    }
  }

  test("Response Conflict(409)") {
    forall(gen) {
      case (pid, payment) =>
        val client = Client.fromHttpApp(routes(Conflict(pid)))

        PaymentClient
          .make[IO](config, client)
          .process(payment)
          .map(expect.same(pid, _))
    }
  }
}
