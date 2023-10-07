data class AppSettings(
    val appUrls: List<String> = emptyList(),
    override val processor: AdProcessor = AdProcessor()
) : IAppSettings
