package workers

import ICorChainDsl
import InnerContext
import models.InnerState.NONE
import models.InnerState.RUNNING
import worker

fun ICorChainDsl<InnerContext>.initStatus(title: String) = worker {
    this.title = title
    on { state == NONE }
    handle { state = RUNNING }
}
