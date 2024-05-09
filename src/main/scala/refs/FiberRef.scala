package refs

import zio.*

final case class Tree[+A](head: A, tail: List[Tree[A]])

type Log = Tree[Chunk[String]]

val loggingRef: ZIO[Scope, Nothing, FiberRef[Log]] =
  FiberRef.make[Log](
    Tree(Chunk.empty, List.empty),
    _ => Tree(Chunk.empty, List.empty),
    (parent, child) => parent.copy(tail = child :: parent.tail)
  )

def log(ref: FiberRef[Log])(string: String): UIO[Unit] =
  ref.update(log => log.copy(head = log.head :+ string))

object FiberRefProgram extends ZIOAppDefault:

  val program =
    for {
      ref <- loggingRef
      left =
        for {
          a <- ZIO.succeed(1).tap(_ => log(ref)("Got 1"))
          b <- ZIO.succeed(2).tap(_ => log(ref)("Got 2"))
        } yield a + b
      right =
        for {
          c <- ZIO.succeed(1).tap(_ => log(ref)("Got 3"))
          d <- ZIO.succeed(2).tap(_ => log(ref)("Got 4"))
        } yield c + d
      fiber1 <- left.fork
      fiber2 <- right.fork
      _ <- fiber1.join
      _ <- fiber2.join
      log <- ref.get
      _ <- Console.printLine(log.toString)
    } yield ()

  val run = program
