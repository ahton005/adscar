package permissions

import ICorChainDsl
import InnerContext
import models.InnerState
import resolveChainPermissions
import worker

fun ICorChainDsl<InnerContext>.chainPermissions(title: String) = worker {
    this.title = title
    description = "Вычисление прав доступа для групп пользователей"

    on { state == InnerState.RUNNING }

    handle {
        permissionsChain.addAll(resolveChainPermissions(principal.groups))
        println("PRINCIPAL: $principal")
        println("PERMISSIONS: $permissionsChain")
    }
}
