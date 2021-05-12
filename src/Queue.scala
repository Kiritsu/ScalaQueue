import scala.annotation.tailrec

case class Queue[T](in: List[T] = Nil, out: List[T] = Nil) {
  /**
   * Enqueues a value at the end of the queue.
   * @param x Value to enqueue.
   * @return The new state of the queue.
   */
  def enqueue(x: T): Queue[T] = {
    Queue[T](x::in, out)
  }

  /**
   * Dequeues the first element of the queue.
   * @return A tuple of the dequeued element and the new state of the queue.
   */
  def dequeue(): (Option[T], Queue[T]) = out match {
    case Nil =>
      if (in.isEmpty)
        return (Option.empty, Queue(Nil, Nil))
      val head :: tail = in.reverse
      (Option(head), Queue[T](Nil, tail))
    case List(_) => (Option(out.head), Queue[T](in, Nil))
    case _ =>
      val head :: tail = out
      (Option(head), Queue[T](in, tail))
  }

  /**
   * Gets the first element (head) of the queue.
   * @return The current head of an empty Option[T].
   */
  def headOption: Option[T] = {
    if (isEmpty) {
      return Option.empty
    }

    if (in.isEmpty) {
      Option(out.last)
    } else {
      Option(in.head)
    }
  }

  /**
   * Gets the last element of the queue.
   * @return The last element of an empty Option[T].
   */
  def rearOption: Option[T] = {
    if (out.nonEmpty) {
      return Option(out.head)
    }

    if (in.isEmpty) {
      return Option.empty
    }

    Option(in.last)
  }

  /**
   * Transforms the Queue[T] into a List[T].
   * @return A List[T] representing the current Queue[T].
   */
  def toList: List[T] = in ::: out.reverse

  /**
   * Maps the elements of the queue with the given function.
   * @param f Function describing the mutation of T into T2 for each value of the queue.
   * @tparam T2 Type to map the values to.
   * @return The new state of the Queue[T2] with every value mapped in T2 with f.
   */
  def map[T2](f: T => T2): Queue[T2] = {
    Queue(in.map(f), out.map(f))
  }

  /**
   * Applies an operation with a constant fallback value for all the element of the Queue from the left to the right.
   * @param const Constant fallback value when encountering Nil.
   * @param f Function that mutates T into T2 during accumulation.
   * @tparam T2 Type that the final value must be mutated to.
   * @return Returns the value that was accumulated during the fold.
   */
  def foldLeft[T2](const: T2)(f: (T2, T) => T2): T2 = {
    @tailrec
    def foldLeft[T3](list: List[T], const: T3)(f: (T3, T) => T3): T3 = list match {
      case Nil => const
      case _ :: tail => foldLeft(tail, const)(f)
    }

    foldLeft(toList, const)(f)
  }

  /**
   * Gets the length of the queue.
   * @return Length of the queue.
   */
  def length: Int = in.length + out.length

  /**
   * Gets whether the queue is empty or not.
   * @return Whether the queue is empty or not.
   */
  def isEmpty: Boolean = out match {
    case Nil => in match {
      case Nil => true
      case _ => false
    }
    case _ => false
  }
}

object Test {
  def printList(x: Queue[Int]): Unit = {
    println("===================")
    println("Contenu de la queue : taille " + x.length)
    println("\t" + x)
    println("As list : ")
    println("\t" + x.toList)
    println("Tête de queue :")
    println("\t" + x.headOption)
    println("Fin de queue :")
    println("\t" + x.rearOption)
    println("Mapping de la queue : *2")
    println("\t" + x.map(y => y * 2))
    if (x.isEmpty) {
      println("La liste est vide !")
    }
    println("===================")
  }

  def printList(x: (Option[Int], Queue[Int])): Unit = {
    println("===================")
    println("Contenu de la queue : taille " + x._2.length)
    println("\t" + x)
    println("As list : ")
    println("\t" + x._2.toList)
    println("Tête de queue :")
    println("\t" + x._2.headOption)
    println("Fin de queue :")
    println("\t" + x._2.rearOption)
    println("Mapping de la queue : *2")
    println("\t" + x._2.map(y => y * 2))
    if (x._2.isEmpty) {
      println("La liste est vide !")
    }
    println("===================")
  }

  def main(args: Array[String]): Unit = {
    val a = Queue[Int](Nil, Nil)
    val b = a.enqueue(1)
    val c = b.enqueue(2)
    val d = c.enqueue(3)
    val f = d.dequeue()
    val g = f._2.enqueue(4)
    val h = g.enqueue(5)
    val i = h.dequeue()
    val j = i._2.dequeue()
    val k = j._2.dequeue()
    val l = k._2.dequeue()
    printList(a)
    printList(b)
    printList(c)
    printList(d)
    printList(f)
    printList(g)
    printList(h)
    printList(i)
    printList(j)
    printList(k)
    printList(l)
    l._2.dequeue()
  }
}

