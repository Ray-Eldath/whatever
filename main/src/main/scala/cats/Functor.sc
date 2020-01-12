/**
 * Functor: F[A] --- A => B ---> F[B]
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

val l = (x: Int) => x * 2
val ll = Functor[Option].lift(l)
ll(Option(123))

