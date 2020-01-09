import scala.language.{implicitConversions, postfixOps}

implicit def toList[A](a: A): List[A] = List(a)

trait Monoid[A] {
  def unit(): A

  def map(a: A, b: A): A
}

implicit object IntAddMonoid extends Monoid[Int] {
  def unit() = 0

  def map(a: Int, b: Int): Int = a + b
}

implicit class RichAny[A](val base: A) extends AnyVal {
  def doThese[B](f: A => B): B = f(base)
}

def add[A: Monoid](l: Seq[A]): A =
  implicitly[Monoid[A]] doThese { n =>
    l.fold(n unit)(n map)
  }

add(1 ::: 2 ::: 3)


