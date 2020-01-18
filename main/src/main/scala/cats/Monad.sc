import scala.collection.mutable.ListBuffer

/**
 * Monad
 *
 * F[A] --- A -> F[B] ---> F[B]
 */
trait Monad[F[_]] {
  def pure[A](value: A): F[A]

  def flatMap[A, B](value: F[A])(func: A => F[B]): F[B]

  // it's easy to prove that every Monad is also a Functor. so we can define:
  def map[A, B](value: F[A])(func: A => B): F[B] =
    flatMap(value)((a: A) => pure(func(a)))
}

object Monad {

  implicit def listMonad: Monad[List] = new Monad[List] {
    override def pure[A](value: A) = List(value)

    override def flatMap[A, B](value: List[A])(func: A => List[B]) = {
      val r = ListBuffer[B]()
      for (v <- value)
        for (b <- func(v))
          r += b

      r.toList
    }
  }

  // I know, I know... you can use for comprehension to avoid these shitty imperative code,
  // it's intentionally: implement this w/o for comprehension

  implicit val optionMonad: Monad[Option] = new Monad[Option] {
    override def pure[A](value: A) = Option(value)

    override def flatMap[A, B](value: Option[A])(func: A => Option[B]) =
      if (value.isDefined) func(value.get) else None
  }

  def apply[F[_]](implicit monad: Monad[F]) = monad
}

val l = List(2, 5, 6)
Monad[List].pure(1)
Monad[List].flatMap(l)(value => List(value + 2))

val s = Option("ray")
Monad[Option].pure("a")
Monad[Option].flatMap(s)(value => Some(value + " eldath"))

// define syntax
object MonadSyntax {

  implicit class MonadOps[A](value: A) {

    def pure[F[_]](implicit monad: Monad[F]) = monad.pure(value)

    def flatMap[B, F[_]](func: A => F[B])(implicit monad: Monad[F]) = monad.flatMap(value.pure)(func)

    def map[B, F[_]](func: A => B)(implicit monad: Monad[F]) = monad.map(value.pure)(func)
  }

}

import MonadSyntax.MonadOps

1.pure[Option]
"ray".pure[List]