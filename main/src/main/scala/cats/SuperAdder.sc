import cats.Monoid
import cats.instances.int._
import cats.instances.option._
import cats.syntax.monoid._

def add[T](items: List[T])(implicit monoid: Monoid[T]): T =
  items.foldLeft(monoid.empty)(_ |+| _)

val l = List(1, 2, 3)

add(l)
add(l.map(Option(_)))

case class Order(totalCost: Double, quantity: Double)

class AddOrderMonoid extends Monoid[Order] {
  override def empty = Order(0, 0)

  override def combine(x: Order, y: Order) = Order(x.totalCost + y.totalCost, x.quantity + y.quantity)
}

implicit val addOrder: Monoid[Order] = new AddOrderMonoid

//

add(List(Order(1.1, 2.2), Order(2.2, 3.3)))