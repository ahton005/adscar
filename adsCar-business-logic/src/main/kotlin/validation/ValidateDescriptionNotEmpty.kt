package validation

import ICorChainDsl
import InnerContext
import helpers.errorValidation
import helpers.fail
import worker

fun ICorChainDsl<InnerContext>.validateDescriptionNotEmpty(title: String) = worker {
    this.title = title
    on { adValidating.description.isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "description",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}
