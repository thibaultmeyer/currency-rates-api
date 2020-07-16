package app

import io.micronaut.runtime.Micronaut

object Application {

    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.build()
            .args(*args)
            .packages("app")
            .mainClass(Application.javaClass)
            .start()
    }
}
