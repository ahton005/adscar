package plugins // ktlint-disable filename

import AppSettings
import io.ktor.server.application.call
import io.ktor.server.http.content.CompressedFileType
import io.ktor.server.http.content.defaultResource
import io.ktor.server.http.content.preCompressed
import io.ktor.server.http.content.resource
import io.ktor.server.http.content.resources
import io.ktor.server.http.content.static
import io.ktor.server.http.content.staticBasePackage
import io.ktor.server.response.respondText
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun Routing.swagger(appConfig: AppSettings) {
    get("/adsCar-{ver}.yaml") {
        val ver = call.parameters["ver"]
        val origTxt: String = withContext(Dispatchers.IO) {
            this::class.java.classLoader
                .getResource("specs/adsCar-$ver.yaml")
                ?.readText()
        } ?: ""
        val response = origTxt.replace(
            Regex(
                "(?<=^servers:\$\\n).*(?=\\ntags:\$)",
                setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.MULTILINE, RegexOption.IGNORE_CASE)
            ),
            appConfig.appUrls.joinToString(separator = "\n") { "  - url: $it$ver" }
        )
        call.respondText { response }
    }

    static("/") {
        preCompressed {
            defaultResource("index.html", "swagger-ui")
            resource("/swagger-initializer.js", "/swagger-initializer.js", "")
            static {
                staticBasePackage = "specs"
                resources(".")
            }
            static {
                preCompressed(CompressedFileType.GZIP) {
                    staticBasePackage = "swagger-ui"
                    resources(".")
                }
            }
        }
    }
}
