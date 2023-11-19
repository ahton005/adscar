data class AppSettings(
    val appUrls: List<String> = emptyList(),
    override val corSettings: MkplCorSettings,
    override val processor: AdProcessor = AdProcessor(corSettings)
) : IAppSettings
