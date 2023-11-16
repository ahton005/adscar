import models.InnerState
import models.InnerWorkMode

fun ICorChainDsl<InnerContext>.prepareResult(title: String) = worker {
    this.title = title
    description = "Подготовка данных для ответа клиенту на запрос"
    on { workMode != InnerWorkMode.STUB }
    handle {
        adResponse = adRepoDone
        adsResponse = adsRepoDone
        state = when (val st = state) {
            InnerState.RUNNING -> InnerState.FINISHING
            else -> st
        }
    }
}
