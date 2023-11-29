import io.ktor.http.HttpMethod.Companion.Get
import io.ktor.http.HttpMethod.Companion.Options
import io.ktor.http.HttpMethod.Companion.Post
import io.ktor.serialization.jackson.jackson
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.cio.EngineMain
import io.ktor.server.http.content.resources
import io.ktor.server.http.content.static
import io.ktor.server.plugins.autohead.AutoHeadResponse
import io.ktor.server.plugins.cachingheaders.CachingHeaders
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.plugins.defaultheaders.DefaultHeaders
import io.ktor.server.response.respondText
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import logging.LogWrapperImpl
import org.slf4j.event.Level
import plugins.initAppSettings
import plugins.swagger
import routing.v1Ad
import ru.zyablov.otus.otuskotlin.adscar.api.v1.apiV1Mapper

private val clazz = Application::moduleJvm::class.qualifiedName ?: "Application"

fun main(args: Array<String>): Unit = EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
fun Application.moduleJvm(appSettings: AppSettings = initAppSettings()) {
    install(CachingHeaders)
    install(DefaultHeaders)
    install(AutoHeadResponse)
    install(Routing)

    install(CORS) {
        allowNonSimpleContentTypes = true
        allowSameOrigin = true
        allowMethod(Options)
        allowMethod(Post)
        allowMethod(Get)
        allowHeader("*")
        appSettings.appUrls.forEach {
            val split = it.split("://")
            println("$split")
            when (split.size) {
                2 -> allowHost(
                    split[1].split("/")[0]/*.apply { log(module = "app", msg = "COR: $this") }*/,
                    listOf(split[0])
                )

                1 -> allowHost(
                    split[0].split("/")[0]/*.apply { log(module = "app", msg = "COR: $this") }*/,
                    listOf("http", "https")
                )
            }
        }
    }

    install(CallLogging) {
        level = Level.INFO
        val lgr = appSettings
            .corSettings
            .loggerProvider
            .logger(clazz) as? LogWrapperImpl
        lgr?.logger?.also { logger = it }
    }

    routing {
        get("/") {
            call.respondText("Hello, world!")
        }

        route("v1") {
            install(ContentNegotiation) {
                jackson {
                    setConfig(apiV1Mapper.serializationConfig)
                    setConfig(apiV1Mapper.deserializationConfig)
                }
            }
            v1Ad(appSettings)
        }

        swagger(appSettings)
        static("static") {
            resources("static")
        }
    }
}
