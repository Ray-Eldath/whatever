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


import cats.Id

object Monad {

  import scala.collection.mutable.ListBuffer

  implicit val listMonad: Monad[List] = new Monad[List] {
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

  implicit val idMonad: Monad[Id] = new Monad[Id] {
    override def pure[A](value: A): Id[A] = value

    override def flatMap[A, B](value: Id[A])(func: A => Id[B]): Id[B] = func(value)
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

  implicit class MonadPureOp[A](value: A) {

    def pure[F[_]](implicit monad: Monad[F]) = monad.pure(value)
  }

  implicit class MonadOps[F[_], A](value: F[A])(implicit monad: Monad[F]) {

    def flatMap1[B](func: A => F[B]) = monad.flatMap(value)(func)

    def map1[B](func: A => B) = monad.map(value)(func)

    // for `for comprehension`:
    def flatMap[B](func: A => F[B]) = flatMap1(func)

    def map[B](func: A => B) = map1(func)
  }

}

import MonadSyntax._

1.pure[Option]
"ray".pure[List]
3.pure[Id]
"eldath".pure[Id]

List("ray ", "beam ").flatMap1(value => List(value + "eldath"))
Option(123).flatMap1(value => Some(value * 2))

import MonadSyntax.MonadOps

def squareAndSum[F[_] : Monad](x: F[Int], y: F[Int]): F[Int] =
  x.flatMap1(a => y.map1(b => a * a + b * b))

squareAndSum(Option(3), Option(4))

// or use for comprehension:
def squareAndSumFor[F[_] : Monad](x: F[Int], y: F[Int]): F[Int] =
  for {
    a <- x
    b <- y
  } yield a * a + b * b

squareAndSum(Option(4), Option(3))