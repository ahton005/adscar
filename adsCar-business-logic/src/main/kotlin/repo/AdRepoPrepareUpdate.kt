import models.InnerState // ktlint-disable filename

fun ICorChainDsl<InnerContext>.repoPrepareUpdate(title: String) = worker {
    this.title = title
    description = "Готовим данные к сохранению в БД: совмещаем данные, прочитанные из БД, " +
        "и данные, полученные от пользователя"
    on { state == InnerState.RUNNING }
    handle {
        adRepoPrepare = adRepoRead.deepCopy().apply {
            this.title = adValidated.title
            description = adValidated.description
            visibility = adValidated.visibility
        }
    }
}
