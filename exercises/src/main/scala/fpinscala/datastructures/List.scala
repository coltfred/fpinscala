package fpinscala.datastructures

sealed trait List[+A] // `List` data type, parameterized on a type, `A`
case object Nil extends List[Nothing] // A `List` data constructor representing the empty list
case class Cons[+A](head: A, tail: List[A]) extends List[A] // Another data constructor, representing nonempty lists. Note that `tail` is another `List[A]`, which may be `Nil` or another `Cons`.

object List { // `List` companion object. Contains functions for creating and working with lists.
  def sum(ints: List[Int]): Int = ints match { // A function that uses pattern matching to add up a list of integers
    case Nil => 0 // The sum of the empty list is 0.
    case Cons(x, xs) => x + sum(xs) // The sum of a list starting with `x` is `x` plus the sum of the rest of the list.
  }

  def product(ds: List[Double]): Double = ds match {
    case Nil => 1.0
    case Cons(0.0, _) => 0.0
    case Cons(x, xs) => x * product(xs)
  }

  def apply[A](as: A*): List[A] = // Variadic function syntax
    if (as.isEmpty) Nil
    else Cons(as.head, apply(as.tail: _*))

  val x = List(1, 2, 3, 4, 5) match {
    case Cons(x, Cons(2, Cons(4, _))) => x
    case Nil => 42
    case Cons(x, Cons(y, Cons(3, Cons(4, _)))) => x + y
    case Cons(h, t) => h + sum(t)
    case _ => 101
  }

  def append[A](a1: List[A], a2: List[A]): List[A] =
    a1 match {
      case Nil => a2
      case Cons(h, t) => Cons(h, append(t, a2))
    }

  def foldRight[A, B](l: List[A], z: B)(f: (A, B) => B): B = // Utility functions
    l match {
      case Nil => z
      case Cons(x, xs) => f(x, foldRight(xs, z)(f))
    }

  def sum2(l: List[Int]) =
    foldRight(l, 0)((x, y) => x + y)

  def product2(l: List[Double]) =
    foldRight(l, 1.0)(_ * _) // `_ * _` is more concise notation for `(x,y) => x * y`, see sidebar

  def tail[A](l: List[A]): List[A] = l match {
    case Nil => sys.error("tail on Nil")
    case Cons(hd, tail) => tail
  }

  def setHead[A](l: List[A])(h: A): List[A] = l match {
    case Nil => Cons(h, Nil)
    case Cons(hd, tail) => Cons(h, tail)
  }

  @annotation.tailrec
  def drop[A](l: List[A], n: Int): List[A] = {
    if (n == 0)
      l
    else {
      l match {
        case Nil => sys.error("drop on Nil")
        case Cons(hd, tail) => drop(tail, n - 1)
      }
    }
  }

  @annotation.tailrec
  def dropWhile[A](l: List[A], f: A => Boolean): List[A] = l match {
    case Cons(hd, tail) if (f(hd)) => dropWhile(tail, f)
    case ll => ll
  }

  def init[A](l: List[A]): List[A] = {
    def go(l: List[A], acc: List[A]): List[A] = l match {
      //We don't want that last item
      case Cons(hd, Nil) => acc
      case Cons(hd, tail) => go(tail, Cons(hd, acc))
      case Nil => sys.error("init on Nil")
    }
    go(l, Nil)
  }

  def length[A](l: List[A]): Int = sys.error("todo")

  def foldLeft[A, B](l: List[A], z: B)(f: (B, A) => B): B = {
    @annotation.tailrec
    def go(b: B, ll: List[A]): B = ll match {
      case Nil => b
      case Cons(hd, tail) => go(f(b, hd), tail)
    }
    go(z, l)
  }

  def map[A, B](l: List[A])(f: A => B): List[B] = {
    @annotation.tailrec
    def go(l: List[A], acc: List[B]): List[B] = l match {
      case Cons(hd, tail) => go(tail, Cons(f(hd), acc))
      case Nil => acc
    }
    go(l, Nil)
  }
}
