/**
 * Invariant Functor
 *
 * invariantMap: F[A] --- A => B, B => A ---> F[B]
 */
trait Codec[A] {
  def encode(value: A): String

  def decode(str: String): A

  def imap[B](dec: A => B, enc: B => A): Codec[B] = new Codec[B] {

    override def encode(value: B): String = Codec.this.encode(enc(value))

    override def decode(str: String): B = dec(Codec.this.decode(str))
  }
}

implicit val stringCodec: Codec[String] = new Codec[String] {
  override def encode(value: String) = "\"" + value + "\""

  override def decode(str: String) = str.replace("\"", "")
}

// once "base type", here is Codec[String], is defined, we can use the invariant map to
// define Codec[B] for every B which has corresponding `B => String` and `String => B`,
// for example, Boolean and Int.
implicit val booleanCodec: Codec[Boolean] = stringCodec.imap(_ == "T", if (_) "T" else "F")
implicit val intCodec: Codec[Int] = stringCodec.imap(_.toInt, _.toString)


// as usual, let's define Syntax for convenience.
object CodecSyntax {

  implicit class CodecEncodeOps[A](val a: A) {

    def encode()(implicit codec: Codec[A]) = codec.encode(a)
  }

  implicit class CodecDecodeOps[A](val str: String) {

    def decode(codec: Codec[A]) = codec.decode(str)
  }

}

import CodecSyntax._

true.encode()
12.encode()

"T".decode(booleanCodec)

// as ever, let's define Codec for the Box
case class Box[A](value: A)

implicit def boxCodex[A](implicit codec: Codec[A]): Codec[Box[A]] = codec.imap(Box(_), _.value)

Box(3).encode()
"F".decode(boxCodex(booleanCodec))