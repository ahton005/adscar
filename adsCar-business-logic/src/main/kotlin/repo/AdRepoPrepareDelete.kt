import models.InnerState // ktlint-disable filename

fun ICorChainDsl<InnerContext>.repoPrepareDelete(title: String) = worker {
    this.title = title
    description = """
        Готовим данные к удалению из БД
    """.trimIndent()
    on { state == InnerState.RUNNING }
    handle {
        adRepoPrepare = adValidated.deepCopy()
    }
}
