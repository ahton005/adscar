import logging.MpLoggerProvider
import repo.IAdRepository

data class AppSettings(
    val appUrls: List<String> = emptyList(),
    override val corSettings: MkplCorSettings,
    override val processor: AdProcessor = AdProcessor(corSettings),
    override val logger: MpLoggerProvider = MpLoggerProvider(),
    override val auth: AuthConfig = AuthConfig.TEST
) : IAppSettings {
    fun getAppSettingsWithRepo(repo: IAdRepository): AppSettings {
        val corSettings = MkplCorSettings(repoTest = repo)
        return AppSettings(
            appUrls = emptyList(),
            corSettings = corSettings,
            processor = AdProcessor(corSettings)
        )
    }
}
