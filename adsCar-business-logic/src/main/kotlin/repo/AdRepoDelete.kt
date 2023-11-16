import models.InnerState // ktlint-disable filename
import repo.DbAdIdRequest

fun ICorChainDsl<InnerContext>.repoDelete(title: String) = worker {
    this.title = title
    description = "Удаление объявления из БД по ID"
    on { state == InnerState.RUNNING }
    handle {
        val request = DbAdIdRequest(adRepoPrepare)
        val result = adRepo.deleteAd(request)
        if (!result.isSuccess) {
            state = InnerState.FAILING
            errors.addAll(result.errors)
        }
        adRepoDone = adRepoRead
    }
}
