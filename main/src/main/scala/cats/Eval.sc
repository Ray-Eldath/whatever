import cats.Eval

val ans = for {
  a <- Eval.now {
    println("Computing a")
    22
  }
  b <- Eval.always {
    println("Computing b")
    20
  }
} yield {
  println("the answer of the universe is...")
  a + b
}

ans.value

ans.value

//noinspection NoTailRecursionAnnotation
def foldRight[A, B](as: List[A], acc: B)(fn: (A, B) => B): B =
  as match {
    case ::(head, next) => foldRight(next, fn(head, acc))(fn)
    case Nil => acc
  }

val l = List(1, 3, 4, 9)
foldRight(l, "")((e, acc) => acc + (e + 3) + " ")

// here comes the problem: w/o @tailrec this is not stack-safe, meaning it may throw SOF
// when dealing with significant huge amount of data:
foldRight((1 to 10000).toList, 0L)(_ + _)
// note that in object or REPL the code will be performed recursive elimination automatically,
// see https://stackoverflow.com/questions/4698827/method-is-tail-recursive-when-defined-on-object-but-not-on-class/4698918

// let's use Eval to make foldRight stack-safe:
def foldRightSafe[A, B](as: List[A], acc: B)(fn: (A, B) => B): Eval[B] =
  as match {
    case ::(head, next) => Eval.defer(foldRightSafe(next, fn(head, acc))(fn))
    case Nil => Eval.now(acc)
  }

foldRightSafe((1 to 10000).toList, 0L)(_ + _).value
