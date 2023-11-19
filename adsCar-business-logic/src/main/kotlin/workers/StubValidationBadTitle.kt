package workers

import ICorChainDsl
import InnerContext
import models.InnerError
import models.InnerState
import stubs.InnerStubs
import worker

fun ICorChainDsl<InnerContext>.stubValidationBadTitle(title: String) = worker {
    this.title = title
    on { stubCase == InnerStubs.BAD_TITLE && state == InnerState.RUNNING }
    handle {
        state = InnerState.FAILING
        this.errors.add(
            InnerError(
                group = "validation",
                code = "validation-title",
                field = "title",
                message = "Wrong title field"
            )
        )
    }
}
