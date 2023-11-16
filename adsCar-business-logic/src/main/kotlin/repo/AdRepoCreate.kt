import models.InnerState // ktlint-disable filename
import repo.DbAdRequest

fun ICorChainDsl<InnerContext>.repoCreate(title: String) = worker {
    this.title = title
    description = "Добавление объявления в БД"
    on { state == InnerState.RUNNING }
    handle {
        val request = DbAdRequest(adRepoPrepare)
        val result = adRepo.createAd(request)
        val resultAd = result.data
        if (result.isSuccess && resultAd != null) {
            adRepoDone = resultAd
        } else {
            state = InnerState.FAILING
            errors.addAll(result.errors)
        }
    }
}
