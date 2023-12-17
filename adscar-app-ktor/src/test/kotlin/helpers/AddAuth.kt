package helpers

import AuthConfig
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders

fun HttpRequestBuilder.addAuth(
    id: String = "userTest",
    groups: List<String> = listOf("USER", "TEST"),
    config: AuthConfig = AuthConfig.TEST
) {
    val token = JWT.create()
        .withAudience(config.audience)
        .withIssuer(config.issuer)
        .withClaim(AuthConfig.GROUPS_CLAIM, groups)
        .withClaim(AuthConfig.ID_CLAIM, id)
        .sign(Algorithm.HMAC256(config.secret))

    header(HttpHeaders.Authorization, "Bearer $token")
}
