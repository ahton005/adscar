package permissions // ktlint-disable filename

import ICorChainDsl
import InnerContext
import chain
import models.InnerSearchPermissions
import models.InnerState
import worker

fun ICorChainDsl<InnerContext>.searchTypes(title: String) = chain {
    this.title = title
    description = "Добавление ограничений в поисковый запрос согласно правам доступа и др. политикам"
    on { state == InnerState.RUNNING }
    worker("Определение типа поиска") {
        adFilterValidated.searchPermissions = setOfNotNull(
            InnerSearchPermissions.OWN.takeIf { permissionsChain.contains(InnerUserPermissions.SEARCH_OWN) },
            InnerSearchPermissions.PUBLIC.takeIf { permissionsChain.contains(InnerUserPermissions.SEARCH_PUBLIC) },
            InnerSearchPermissions.REGISTERED.takeIf { permissionsChain.contains(InnerUserPermissions.SEARCH_REGISTERED) }
        ).toMutableSet()
    }
}
