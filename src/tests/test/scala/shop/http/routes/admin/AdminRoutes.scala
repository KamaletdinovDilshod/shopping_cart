package shop.http.routes.admin

import cats.data.Kleisli
import cats.effect.IO
import org.http4s.Method.GET
import org.http4s.server.AuthMiddleware
import shop.generators.{ cartTotalGen, commonUserGen }
import shop.http.authentication.users.CommonUser
import shop.http.routes.secured.CartRoutesSuite
import shop.suite.HttpSuite

object AdminRoutes extends HttpSuite {}
