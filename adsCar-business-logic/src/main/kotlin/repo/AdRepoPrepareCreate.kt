import models.InnerState // ktlint-disable filename

fun ICorChainDsl<InnerContext>.repoPrepareCreate(title: String) = worker {
    this.title = title
    description = "Подготовка объекта к сохранению в базе данных"
    on { state == InnerState.RUNNING }
    handle {
        adRepoRead = adValidated.deepCopy()
        adRepoRead.ownerId = principal.id
        adRepoPrepare = adRepoRead
    }
}
