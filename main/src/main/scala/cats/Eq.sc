/**
 * Eq is for type-safety equality. With Eq invoking `Some(1) === 1`
 * will cause compile-time error instead of get result `false`, for
 * default equal behavior is compare by reference. As a a result,
 * type-safety is not addressed in core Scala.
 */

import java.time.LocalDate

import cats.Eq
import cats.instances.int._
import cats.instances.option._
import cats.syntax.eq._
import cats.syntax.option._

val options = List(1, 2, 5).map(Some(_))

options.filter(_ == 2) // nothing will you get because of comparing by reference

// options.filter(_ === 2)
// not compiled due to type checker: this is type safety
options.filter((t: Option[Int]) => t === 2.some) // this works fine

1 === 2

1.some === none[Int]
1.some === 2.some
2.some === 2.some

// now let's define our custom equal for LocalDate

object EqInstances {
  implicit val localDateEq: Eq[LocalDate] =
    Eq.instance((a, b) => a.isEqual(b))
}

import EqInstances._

LocalDate.now() === LocalDate.now()
LocalDate.now() === LocalDate.MAX
LocalDate.now() === null