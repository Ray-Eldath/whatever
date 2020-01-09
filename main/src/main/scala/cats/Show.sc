/**
 * Show is for print, or format any class into String in a
 * functional programming, specifically type class manner.
 */

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import cats.Show

// we import implicit defined instance for Int, then
// defined an instance ourselves for String and LocalDate.
import cats.instances.int._

val showInt = Show[Int]

object ShowInstances {
  implicit val showStringInstance: Show[String] = (s: String) => s

  // or use convenient method on Show:
  implicit val showDateInstance: Show[LocalDate] = Show.show((date: LocalDate) =>
    date.format(DateTimeFormatter.ISO_LOCAL_DATE))
}

import ShowInstances._

val showString = Show[String]
val showDate = Show[LocalDate]

showInt.show(123)
showString.show("123")
showDate.show(LocalDate.now())

// use syntax for convenience

import cats.syntax.show._

123.show
"123".show
LocalDate.now().show