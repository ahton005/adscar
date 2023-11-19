package validation

import ICorChainDsl
import InnerContext
import helpers.errorValidation
import helpers.fail
import worker

// TODO-validation-7: пример обработки ошибки в рамках бизнес-цепочки
fun ICorChainDsl<InnerContext>.validateDescriptionHasContent(title: String) = worker {
    this.title = title
    val regExp = Regex("\\p{L}")
    on { adValidating.description.isNotEmpty() && !adValidating.description.contains(regExp) }
    handle {
        fail(
            errorValidation(
                field = "description",
                violationCode = "noContent",
                description = "field must contain letters"
            )
        )
    }
}
