import models.InnerState // ktlint-disable filename
import repo.DbAdIdRequest

fun ICorChainDsl<InnerContext>.repoRead(title: String) = worker {
    this.title = title
    description = "Чтение объявления из БД"
    on { state == InnerState.RUNNING }
    handle {
        val request = DbAdIdRequest(adValidated)
        val result = adRepo.readAd(request)
        val resultAd = result.data
        if (result.isSuccess && resultAd != null) {
            adRepoRead = resultAd
        } else {
            state = InnerState.FAILING
            errors.addAll(result.errors)
        }
    }
}
