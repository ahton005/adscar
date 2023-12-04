package permissions

import ICorChainDsl
import InnerContext
import chain
import checkPermitted
import helpers.fail
import models.InnerError
import models.InnerState
import resolveRelationsTo
import worker

fun ICorChainDsl<InnerContext>.accessValidation(title: String) = chain {
    this.title = title
    description = "Вычисление прав доступа по группе принципала и таблице прав доступа"

    on { state == InnerState.RUNNING }

    worker("Вычисление отношения объявления к принципалу") {
        adRepoRead.relations = adRepoRead.resolveRelationsTo(principal)
    }

    worker("Вычисление доступа к объявлению") {
        permitted = checkPermitted(command, adRepoRead.relations, permissionsChain)
    }

    worker {
        this.title = "Валидация прав доступа"
        description = "Проверка наличия прав для выполнения операции"
        on { !permitted }
        handle {
            fail(InnerError(message = "User is not allowed to perform this operation"))
        }
    }
}
