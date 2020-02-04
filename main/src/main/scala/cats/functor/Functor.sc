/**
 * Functor
 *
 * map (covariance): F[A] --- A => B ---> F[B]
 */

trait Functor[F[_]] {
  def map[A, B](a: F[A])(f: A => B): F[B]

  def lift[A, B](f: A => B): F[A] => F[B] = map(_)(f)
}

object Functor {

  implicit val listFunctor: Functor[List] = new Functor[List] {

    override def map[A, B](a: List[A])(f: A => B): List[B] = {
      for {
        e <- a
        b = f(e)
      } yield b
    }
  }

  implicit val optionFunctor: Functor[Option] = new Functor[Option] {

    override def map[A, B](a: Option[A])(f: A => B) = Option(f(a.get))
  }

  def apply[F[_]](implicit f: Functor[F]) = f
}


val l = List(1, 2, 3)

Functor[List].map(l)(_ * 2)
Functor[Option].map(Option(2))(_ * 2)

val i = (x: Int) => x * 2
val ii = Functor[Option].lift(i)
ii(Option(123))

// use syntax
object FunctorSyntax {

  implicit class FunctorOps[A, F[_]](a: F[A]) {

    def map1[B](f: A => B)(implicit functor: Functor[F]): F[B] =
      functor.map(a)(f)
  }

}

import FunctorSyntax.FunctorOps

l.map1(_ * 2)

// Functor for binary tree
trait Tree[+A]

final case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]

object Branch {
  def apply[A](left: Tree[A], right: Tree[A]): Tree[A] = new Branch(left, right)
}

final case class Leaf[A](value: A) extends Tree[A]

object Leaf {
  def apply[A](value: A): Tree[A] = new Leaf(value)
}

implicit val binaryTreeFunctor: Functor[Tree] = new Functor[Tree] {

  override def map[A, B](a: Tree[A])(f: A => B): Tree[B] = a match {
    case Branch(left, right) => Branch(map(left)(f), map(right)(f))
    case Leaf(value) => Leaf(f(value))
  }
}

Branch(Leaf(10), Leaf(20)).map1(_ * 2)