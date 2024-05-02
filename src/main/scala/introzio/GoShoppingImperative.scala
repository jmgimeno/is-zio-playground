package introzio

import java.util.concurrent.{Executors, ScheduledExecutorService, TimeUnit}
import java.util.concurrent.TimeUnit.*

object GoShoppingImperative:

  @main def imperative() =

    def goShoppingUnsafe: Unit =
      println("Going to the grocery store")

    val scheduler: ScheduledExecutorService =
      Executors.newScheduledThreadPool(1)

    println("before")
    scheduler.schedule(
      new Runnable { def run: Unit = goShoppingUnsafe },
      10,
      TimeUnit.SECONDS
    )
    println("after")

    scheduler.shutdown()
