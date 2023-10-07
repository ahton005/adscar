package plugins

import AdProcessor
import AppSettings
import io.ktor.server.application.Application

fun Application.initAppSettings(): AppSettings {
    return AppSettings(
        appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        processor = AdProcessor()
    )
}
