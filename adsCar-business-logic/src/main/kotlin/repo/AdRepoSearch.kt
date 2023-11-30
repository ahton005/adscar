import models.InnerState // ktlint-disable filename
import repo.DbAdFilterRequest

fun ICorChainDsl<InnerContext>.repoSearch(title: String) = worker {
    this.title = title
    description = "Поиск объявлений в БД по фильтру"
    on { state == InnerState.RUNNING }
    handle {
        val request = DbAdFilterRequest(
            titleFilter = adFilterValidated.searchString,
            ownerId = adFilterValidated.ownerId
        )
        val result = adRepo.searchAd(request)
        val resultAds = result.data
        if (result.isSuccess && resultAds != null) {
            adsRepoDone = resultAds.toMutableList()
        } else {
            state = InnerState.FAILING
            errors.addAll(result.errors)
        }
    }
}
