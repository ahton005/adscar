package workers

import ICorChainDsl
import InnerContext
import models.InnerError
import models.InnerState
import stubs.InnerStubs
import worker

fun ICorChainDsl<InnerContext>.stubValidationBadId(title: String) = worker {
    this.title = title
    on { stubCase == InnerStubs.BAD_ID && state == InnerState.RUNNING }
    handle {
        this.state = InnerState.FAILING
        this.errors.add(
            InnerError(
                group = "validation",
                code = "validation-id",
                field = "id",
                message = "Wrong id field"
            )
        )
    }
}
