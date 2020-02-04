import cats.data.Reader

case class Cat(name: String, favouriteFood: String)

val anAdorableCat = Cat("Ray Eldath", "steak")

val catName: Reader[Cat, String] = Reader(_.name)
catName.run(anAdorableCat)
// work as FunctionWrapper[-A, +B](A => B)

val feed: Reader[Cat, String] = Reader(cat => s"Have a nice bowl of ${cat.favouriteFood}")
feed.run(anAdorableCat)

val eat: Reader[Cat, String] = catName.map(name => s"eat you, $name!")

val feedAndEat = for {
  f <- feed
  e <- eat
} yield s"$f, then $e"
feedAndEat(anAdorableCat)

val feedAndEatWoForComp = feed.flatMap(f => eat.map(e => s"$f, then $e"))
feedAndEatWoForComp(anAdorableCat)

//

case class Db(
               usernames: Map[Int, String],
               passwords: Map[String, String]
             )

type DbReader[T] = Reader[Db, T]

def findUsername(userId: Int): DbReader[Option[String]] =
  Reader(db => db.usernames.get(userId))
def checkPassword(username: String, password: String): DbReader[Boolean] =
  Reader(db => db.passwords.get(username).contains(password))

// lead to:

import cats.syntax.applicative._

def checkLogin(userId: Int, password: String): DbReader[Boolean] =
  for {
    u <- findUsername(userId)
    a = u.map(checkPassword(_, password))
    b <- a.getOrElse(false.pure[DbReader])
  } yield b


// test
val users = Map(1 -> "dade", 2 -> "kate", 3 -> "margo")
val passwords = Map("dade" -> "zerocool", "kate" -> "acidburn", "margo" -> "secret")
val db = Db(users, passwords)

checkLogin(1, "zerocool").run(db)
checkLogin(4, "davinci").run(db)
