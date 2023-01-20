package shop.services

import cats.effect.Resource
import cats.effect.kernel.MonadCancelThrow
import cats.syntax.all._
import skunk._
import skunk.syntax.all._
import shop.domain.ID
import shop.domain.category._
import shop.effects.GenUUID
import shop.sql.codecs._

trait Categories[F[_]] {
  def findAll: F[List[Category]]
  def create(name: CategoryName): F[CategoryId]
}

object Categories {
  import CategorySQL._
  def make[F[_]: GenUUID: MonadCancelThrow](
      postres: Resource[F, Session[F]]
  ): Categories[F] =
    new Categories[F] {
      override def findAll: F[List[Category]] =
        postres.use(_.execute(selectAll))

      override def create(name: CategoryName): F[CategoryId] =
        postres.use { session =>
          session.prepare(insertCategory).use{cmd =>
            ID.make[F, CategoryId].flatMap {id =>
              cmd.execute(Category(id, name)).as(id)
            }
          }
        }
    }
}

private object CategorySQL {
  val codec: Codec[Category] =
    (categoryId ~ categoryName).imap {
      case i ~ n => Category(i, n)
    }(c => c.uuid ~ c.name)

  val selectAll: Query[Void, Category] =
    sql"""
         SELECT * FROM categories
       """.query(codec)

  val insertCategory: Command[Category] =
    sql"""
         INSERT INTO categories
         VALUES ($codec)
       """.command
}
