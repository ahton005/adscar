package groups

import ICorChainDsl
import InnerContext
import chain
import models.InnerState.RUNNING
import models.InnerWorkMode.STUB

fun ICorChainDsl<InnerContext>.stubs(title: String, block: ICorChainDsl<InnerContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { workMode == STUB && state == RUNNING }
}
