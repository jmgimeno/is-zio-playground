package refs

import zio.*

// One important use of `Ref` is to implement stateful APIs

final case class User(id: Long, name: String)
final case class Book(isbn: String, title: String)

enum LibraryError:
  case UserExists(user: User)
  case BookExists(book: Book)

import LibraryError.*

trait Library:
  def addUser(user: User): ZIO[Any, UserExists, Unit]
  def addBook(book: Book): ZIO[Any, BookExists, Unit]
  def getUser(id: Long): ZIO[Any, Unit, User]
  def getBook(isbn: String): ZIO[Any, Unit, Book]

object Library:
  def make: ZIO[Any, Nothing, Library] =
    case class LibraryState(users: Map[Long, User], books: Map[String, Book])
    Ref.make(LibraryState(Map.empty, Map.empty)).map { ref =>
      new Library:
        def addUser(user: User): ZIO[Any, UserExists, Unit] =
          ref.modify { oldState =>
            if oldState.users.contains(user.id)
            then (Left[UserExists, Unit](UserExists(user)), oldState)
            else
              (
                Right(()),
                oldState.copy(users = oldState.users + (user.id -> user))
              )
          }.absolve

        def addBook(book: Book): ZIO[Any, BookExists, Unit] =
          ref.modify { oldState =>
            if oldState.books.contains(book.isbn)
            then (Left[BookExists, Unit](BookExists(book)), oldState)
            else
              (
                Right(()),
                oldState.copy(books = oldState.books + (book.isbn -> book))
              )
          }.absolve

        def getUser(id: Long): ZIO[Any, Unit, User] =
          ref.get.flatMap { state =>
            ZIO.attempt(state.users(id)).mapError(_ => ())
          }

        def getBook(isbn: String): ZIO[Any, Unit, Book] =
          ref.get.flatMap { state =>
            ZIO.attempt(state.books(isbn)).mapError(_ => ())
          }
    }

object LibraryProgram extends ZIOAppDefault:

  val program =
    for
      library <- Library.make
      _ <- library.addUser(User(1, "John"))
      _ <- library.addUser(User(2, "Mary"))
      _ <- library.addBook(Book("1234", "The Lord of the Rings"))
      _ <- library.addBook(Book("5678", "The Hobbit"))
      user <- library.getUser(1)
      _ <- Console.printLine(s"user= $user")
      book <- library.getBook("1234")
      _ <- Console.printLine(s"book= $book")
      _ <- library
        .addUser(User(1, "John"))
        .foldZIO(
          err => Console.printLine(s"error= $err"),
          _ => Console.printLine("user added")
        )
    yield ()

  val run = program
