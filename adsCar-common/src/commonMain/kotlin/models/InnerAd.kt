package models

import permissions.InnerPrincipalRelations
import java.math.BigDecimal
import java.math.BigDecimal.ZERO

data class InnerAd(
    var id: InnerAdId = InnerAdId.NONE,
    var title: String = "",
    var description: String = "",
    var visibility: InnerVisibility = InnerVisibility.NONE,
    var logos: List<String> = listOf(),
    var ownerId: InnerUserId = InnerUserId.NONE,
    var price: BigDecimal = ZERO,
    var permissionsClient: MutableSet<InnerAdPermissionClient> = mutableSetOf(),
    var lock: InnerAdLock = InnerAdLock.NONE,
    var relations: Set<InnerPrincipalRelations> = emptySet()
) {
    fun deepCopy(): InnerAd = copy(
        permissionsClient = permissionsClient.toMutableSet()
    )

    companion object {
        val NONE get() = InnerAd()
    }
}
