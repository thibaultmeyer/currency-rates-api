package app

import io.micronaut.runtime.EmbeddedApplication
import io.micronaut.test.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import javax.inject.Inject

@MicronautTest
class ApplicationTest {

    @Inject
    lateinit var application: EmbeddedApplication<*>

    @Test
    fun applicationIsRunning() {
        Assertions.assertTrue(application.isRunning)
    }

    @Test
    fun applicationIsServer() {
        Assertions.assertTrue(application.isServer)
    }
}
