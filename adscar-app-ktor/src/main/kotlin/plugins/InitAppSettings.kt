package plugins

import AdProcessor
import AppSettings
import MkplCorSettings
import io.ktor.server.application.Application

fun Application.initAppSettings(): AppSettings {
    val corSettings = MkplCorSettings(
        loggerProvider = getLoggerProviderConf()
    )
    return AppSettings(
        appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        processor = AdProcessor(corSettings),
        corSettings = corSettings
    )
}
