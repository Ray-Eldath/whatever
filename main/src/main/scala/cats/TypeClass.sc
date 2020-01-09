/**
 * Type class, with many other well-defined concepts forms the very
 * basic of functional programming, which is somewhat regarded as the
 * equivalent of design patterns in object-oriented programming. Type
 * class allows you to broaden the public interface of a class without
 * modifying it, or through inheritance.
 */
case class Person(name: String, age: Int)

sealed class Json

trait JsonSerializer[T] {
  def write(json: T): String
}

// way 1: instance object

object JsonSerializer { // implicit scope
  implicit val serializePerson: JsonSerializer[Person] =
    (json: Person) => s"""{ "name": "${json.name}", "age": ${json.age} }"""
}

object Json {
  def serialize[T](value: T)(implicit serializer: JsonSerializer[T]) = serializer.write(value)
}

Json serialize Person("Ray Eldath", 16)

// or, use extension function

object JsonSyntax {

  implicit class JsonSerializeExtension[T](p: T)(implicit serializer: JsonSerializer[T]) {
    def serialize() = serializer.write(p)
  }

}

import JsonSyntax._

Person("Ray Eldath", 16).serialize()
