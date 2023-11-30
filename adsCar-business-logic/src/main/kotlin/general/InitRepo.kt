import helpers.errorAdministration
import helpers.fail
import models.InnerWorkMode
import repo.IAdRepository

fun ICorChainDsl<InnerContext>.initRepo(title: String) = worker {
    this.title = title
    description = """
        Вычисление основного рабочего репозитория в зависимости от зпрошенного режима работы        
    """.trimIndent()
    handle {
        adRepo = when {
            workMode == InnerWorkMode.TEST -> settings.repoTest
            workMode == InnerWorkMode.STUB -> settings.repoStub
            else -> settings.repoProd
        }
        if (workMode != InnerWorkMode.STUB && adRepo == IAdRepository.NONE) fail(
            errorAdministration(
                field = "repo",
                violationCode = "dbNotConfigured",
                description = "The database is unconfigured for chosen workmode ($workMode). Please, contact the administrator staff"
            )
        )
    }
}
