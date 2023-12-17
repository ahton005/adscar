package base // ktlint-disable filename

import AuthConfig.Companion.F_NAME_CLAIM
import AuthConfig.Companion.GROUPS_CLAIM
import AuthConfig.Companion.ID_CLAIM
import AuthConfig.Companion.L_NAME_CLAIM
import AuthConfig.Companion.M_NAME_CLAIM
import io.ktor.server.auth.jwt.JWTPrincipal
import models.InnerUserId
import permissions.InnerPrincipalModel
import permissions.InnerUserGroups

fun JWTPrincipal?.toModel() = this?.run {
    InnerPrincipalModel(
        id = payload.getClaim(ID_CLAIM).asString()?.let { InnerUserId(it) } ?: InnerUserId.NONE,
        fname = payload.getClaim(F_NAME_CLAIM).asString() ?: "",
        mname = payload.getClaim(M_NAME_CLAIM).asString() ?: "",
        lname = payload.getClaim(L_NAME_CLAIM).asString() ?: "",
        groups = payload
            .getClaim(GROUPS_CLAIM)
            ?.asList(String::class.java)
            ?.mapNotNull {
                when (it) {
                    "USER" -> InnerUserGroups.USER
                    "TEST" -> InnerUserGroups.TEST
                    else -> null
                }
            }?.toSet() ?: emptySet()
    )
} ?: InnerPrincipalModel.NONE
