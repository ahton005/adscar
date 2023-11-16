import logging.MpLoggerProvider
import repo.IAdRepository

data class MkplCorSettings(
    val loggerProvider: MpLoggerProvider = MpLoggerProvider(),

    val repoStub: IAdRepository = IAdRepository.NONE,
    val repoTest: IAdRepository = IAdRepository.NONE,
    val repoProd: IAdRepository = IAdRepository.NONE
) {
    companion object {
        val NONE = MkplCorSettings()
    }
}
