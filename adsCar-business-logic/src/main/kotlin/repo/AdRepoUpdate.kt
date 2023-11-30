import models.InnerState // ktlint-disable filename
import repo.DbAdRequest

fun ICorChainDsl<InnerContext>.repoUpdate(title: String) = worker {
    this.title = title
    on { state == InnerState.RUNNING }
    handle {
        val request = DbAdRequest(adRepoPrepare)
        val result = adRepo.updateAd(request)
        val resultAd = result.data
        if (result.isSuccess && resultAd != null) {
            adRepoDone = resultAd
        } else {
            state = InnerState.FAILING
            errors.addAll(result.errors)
            adRepoDone
        }
    }
}
