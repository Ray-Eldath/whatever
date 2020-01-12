trait Semigroup[A] {
  def combine(x: A, y: A): A
}

trait Monoid[A] extends Semigroup[A] {
  def unit: A
}

object Monoid {
  def apply[A](implicit monoid: Monoid[A]) = monoid
}


class Or extends Monoid[Boolean] {
  override def unit = false

  override def combine(x: Boolean, y: Boolean) = x || y
}

class And extends Monoid[Boolean] {
  override def unit: Boolean = true

  override def combine(x: Boolean, y: Boolean): Boolean = x && y
}

class Xor extends Monoid[Boolean] {
  override def unit = true

  override def combine(x: Boolean, y: Boolean) = x == y
}