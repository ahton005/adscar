package helpers // ktlint-disable filename

import models.InnerError

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
