package variance

import zio.*

object Covariance:

  trait Gen[-R, +A]:
    def sample: ZIO[R, Nothing, A]
    // def contains(a: A): ZIO[R, Nothing, Boolean]
