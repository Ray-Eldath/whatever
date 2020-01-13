/**
 * Contravariant Functor
 *
 * contraMap: F[A] --- B => A ---> F[B]
 */
trait Printable[A] {
  def format(value: A): String

  def contraMap[B](func: B => A): Printable[B] = (value: B) => Printable.this.format(func(value))
}

implicit val booleanPrintable: Printable[Boolean] = (value: Boolean) => value.toString.toUpperCase()

implicit val stringPrintable: Printable[String] = (value: String) => "\"" + value + "\""

object PrintableSyntax {

  implicit class PrintableOps[A](a: A)(implicit f: Printable[A]) {
    def format1() = f.format(a)
  }

}

import PrintableSyntax.PrintableOps

"123".format1()
true.format1()

// next step, we want Box to be printable as well:
final case class Box[+A](value: A)

// here's the intuitive solution:

// implicit def boxPrintable[A](implicit f: Printable[A]): Printable[Box[A]] = (value: Box[A]) => f.format(value.value)

// while use `contraMap` produces terser code:
implicit def boxPrintable[A](implicit f: Printable[A]): Printable[Box[A]] = f.contraMap[Box[A]](_.value)

Box("123").format1()
Box(true).format1()
