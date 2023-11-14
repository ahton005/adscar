package workers

import ICorChainDsl
import InnerContext
import helpers.fail
import models.InnerError
import models.InnerState
import worker

fun ICorChainDsl<InnerContext>.stubNoCase(title: String) = worker {
    this.title = title
    on { state == InnerState.RUNNING }
    handle {
        fail(
            InnerError(
                code = "validation",
                field = "stub",
                group = "validation",
                message = "Wrong stub case is requested: ${stubCase.name}"
            )
        )
    }
}
