package workers

import ICorChainDsl
import InnerContext
import models.InnerError
import models.InnerState
import stubs.InnerStubs
import worker

fun ICorChainDsl<InnerContext>.stubDbError(title: String) = worker {
    this.title = title
    on { stubCase == InnerStubs.DB_ERROR && state == InnerState.RUNNING }
    handle {
        state = InnerState.FAILING
        this.errors.add(
            InnerError(
                group = "internal",
                code = "internal-db",
                message = "Internal error"
            )
        )
    }
}
