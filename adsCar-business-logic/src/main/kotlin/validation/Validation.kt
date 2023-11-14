package validation

import ICorChainDsl
import InnerContext
import chain
import models.InnerState.RUNNING

fun ICorChainDsl<InnerContext>.validation(block: ICorChainDsl<InnerContext>.() -> Unit) = chain {
    block()
    title = "Валидация"
    on { state == RUNNING }
}
