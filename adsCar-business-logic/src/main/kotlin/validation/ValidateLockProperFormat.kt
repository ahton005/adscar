package validation

import ICorChainDsl
import InnerContext
import helpers.errorValidation
import helpers.fail
import models.InnerAdLock
import worker

fun ICorChainDsl<InnerContext>.validateLockProperFormat(title: String) = worker {
    this.title = title

    // Может быть вынесен в InnerAdId для реализации различных форматов
    val regExp = Regex("^[0-9a-zA-Z-]+$")
    on { adValidating.lock != InnerAdLock.NONE && !adValidating.lock.asString().matches(regExp) }
    handle {
        val encodedId = adValidating.lock.asString()
        fail(
            errorValidation(
                field = "lock",
                violationCode = "badFormat",
                description = "value $encodedId must contain only"
            )
        )
    }
}
