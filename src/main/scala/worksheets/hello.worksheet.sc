import zio.*
import hello.HelloZIO

val runtime = zio.Runtime.default

val program = ZIO.attempt(println("patata"))
// ^^^^^ transparència referencial (descripcions)
// ===================
val result = Unsafe.unsafe { unsafe ?=>
  // vvvvv No hi ha transparència referencial
  runtime.unsafe.run(program)
}
