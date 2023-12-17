import logging.MpLoggerProvider

interface IAppSettings {
    val processor: AdProcessor
    val corSettings: MkplCorSettings
    val logger: MpLoggerProvider
    val auth: AuthConfig
}
