package validation

import ICorChainDsl
import InnerContext
import helpers.errorValidation
import helpers.fail
import worker

fun ICorChainDsl<InnerContext>.validateLockNotEmpty(title: String) = worker {
    this.title = title
    on { adValidating.lock.asString().isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "lock",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}
