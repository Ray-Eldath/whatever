import cats.data.{Writer, WriterT}
import cats.instances.vector._
import cats.syntax.applicative._
import cats.syntax.writer._

val v = Vector("It was the best of times", "it was the worst of times")
Writer(v, 1859)
Writer(1859, v)

type Logged[R] = Writer[Vector[String], R]
123.pure[Logged]

v.tell

val a = 123.writer(v)
a.value
a.written

val (value, written) = a.run

val w = WriterT(Some(v, 123))
w.run

val writer1 = for {
  a <- 12.pure[Logged]
  _ <- Vector("a").tell
  _ <- v.tell
  b <- 42.writer(Vector("b", "c"))
} yield a + b

val (value1, written1) = writer1.mapWritten(_.map(_.toUpperCase())).run
value1
written1

writer1.swap

writer1.swap
writer1 // immutability: just return a new Writer

writer1.reset
writer1

//

def slowly[A](body: => A) = try body finally Thread.sleep(100)
def factorial(n: Int): Int = {
  val ans = slowly {
    if (n == 0) 1 else n * factorial(n - 1)
  }
  println(s"fact $n $ans")
  ans
}

factorial(5)


type IntLogged[R] = Writer[Vector[Int], R]
def factorialW(n: IntLogged[Int]): IntLogged[Int] = {
  val a = slowly {
    val (log, ans) = n.run
    val b = ans - 1

    if (ans == 0) 1.pure[IntLogged]
    else
      for {
        a <- n
        _ <- Vector(b).tell
        b <- factorialW(b.writer(log))
      } yield a * b
  }
  a
}
factorialW(5.pure[IntLogged])

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

Await.result(Future.sequence(Vector(
  Future(factorialW(4.pure[IntLogged])),
  Future(factorialW(2.pure[IntLogged]))
)), 10.seconds)