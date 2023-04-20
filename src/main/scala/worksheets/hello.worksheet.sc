import zio.*
import hello.HelloZIO

val runtime = zio.Runtime.default

val result = Unsafe.unsafe { implicit unsafe =>
  runtime.unsafe.run(HelloZIO.run)
}
