package plugins

import AdProcessor
import AdRepoStub
import AppSettings
import MkplCorSettings
import io.ktor.server.application.Application

fun Application.initAppSettings(): AppSettings {
    val corSettings = MkplCorSettings(
        loggerProvider = getLoggerProviderConf(),
        repoTest = getDatabaseConf(AdDbType.TEST),
        repoProd = getDatabaseConf(AdDbType.PROD),
        repoStub = AdRepoStub()
    )
    return AppSettings(
        appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        processor = AdProcessor(corSettings),
        corSettings = corSettings
    )
}
