import repo.IAdRepository

data class AppSettings(
    val appUrls: List<String> = emptyList(),
    override val corSettings: MkplCorSettings,
    override val processor: AdProcessor = AdProcessor(corSettings)
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
