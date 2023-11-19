package validation

import ICorChainDsl
import InnerContext
import helpers.errorValidation
import helpers.fail
import worker

fun ICorChainDsl<InnerContext>.validateTitleHasContent(title: String) = worker {
    this.title = title
    val regExp = Regex("\\p{L}")
    on { adValidating.title.isNotEmpty() && !adValidating.title.contains(regExp) }
    handle {
        fail(
            errorValidation(
                field = "title",
                violationCode = "noContent",
                description = "field must contain leters"
            )
        )
    }
}
