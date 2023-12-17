package permissions

import ICorChainDsl
import InnerContext
import models.InnerState
import resolveFrontPermissions
import resolveRelationsTo
import worker

fun ICorChainDsl<InnerContext>.frontPermissions(title: String) = worker {
    this.title = title
    description = "Вычисление разрешений пользователей для фронтенда"

    on { state == InnerState.RUNNING }

    handle {
        adRepoDone.permissionsClient.addAll(
            resolveFrontPermissions(
                permissionsChain,
                // Повторно вычисляем отношения, поскольку они могли измениться при выполнении операции
                adRepoDone.resolveRelationsTo(principal)
            )
        )

        adsRepoDone.forEach {
            it.permissionsClient.addAll(
                resolveFrontPermissions(
                    permissionsChain,
                    it.resolveRelationsTo(principal)
                )
            )
        }
    }
}
