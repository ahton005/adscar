package validation

import ICorChainDsl
import InnerContext
import helpers.errorValidation
import helpers.fail
import worker

// TODO-validation-4: смотрим пример COR DSL валидации
fun ICorChainDsl<InnerContext>.validateTitleNotEmpty(title: String) = worker {
    this.title = title
    on { adValidating.title.isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "title",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}
