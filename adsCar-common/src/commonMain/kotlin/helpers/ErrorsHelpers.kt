package helpers // ktlint-disable filename

import InnerContext
import models.InnerError
import models.InnerState.FAILING

fun Throwable.asError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: ""
) = InnerError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this
)

fun InnerContext.addError(vararg error: InnerError) = errors.apply { addAll(error) }

fun InnerContext.fail(error: InnerError) {
    errors = addError(error)
    state = FAILING
}

fun errorValidation(
    field: String,
    /**
     * Код, характеризующий ошибку. Не должен включать имя поля или указание на валидацию.
     * Например: empty, badSymbols, tooLong, etc
     */
    violationCode: String,
    description: String,
    level: InnerError.Level = InnerError.Level.ERROR
) = InnerError(
    code = "validation-$field-$violationCode",
    field = field,
    group = "validation",
    message = "Validation error for field $field: $description",
    level = level
)
