package auth

import AuthConfig
import helpers.addAuth
import helpers.testSettings
import io.ktor.client.request.post
import io.ktor.server.testing.testApplication
import moduleJvm
import org.junit.Test
import kotlin.test.assertEquals

class AuthTest {
    @Test
    fun invalidAudience() = testApplication {
        application {
            moduleJvm(testSettings())
        }

        val response = client.post("/v1/ad/create") {
            addAuth(config = AuthConfig.TEST.copy(audience = "invalid"))
        }
        assertEquals(401, response.status.value)
    }
}
