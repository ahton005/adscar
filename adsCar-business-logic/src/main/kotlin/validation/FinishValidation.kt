package validation

import ICorChainDsl
import InnerContext
import models.InnerState.RUNNING
import worker

fun ICorChainDsl<InnerContext>.finishAdValidation(title: String) = worker {
    this.title = title
    on { state == RUNNING }
    handle {
        adValidated = adValidating
    }
}

fun ICorChainDsl<InnerContext>.finishAdFilterValidation(title: String) = worker {
    this.title = title
    on { state == RUNNING }
    handle {
        adFilterValidated = adFilterValidating
    }
}
