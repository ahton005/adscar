package permissions

import models.InnerUserId

data class InnerPrincipalModel(
    val id: InnerUserId = InnerUserId.NONE,
    val fname: String = "",
    val mname: String = "",
    val lname: String = "",
    val groups: Set<InnerUserGroups> = emptySet()
) {
    companion object {
        val NONE = InnerPrincipalModel()
    }
}
