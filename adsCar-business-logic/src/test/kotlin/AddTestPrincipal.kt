import models.InnerUserId
import permissions.InnerPrincipalModel
import permissions.InnerUserGroups

fun InnerContext.addTestPrincipal(userId: InnerUserId = InnerUserId("321")) {
    principal = InnerPrincipalModel(
        id = userId,
        groups = setOf(InnerUserGroups.USER, InnerUserGroups.TEST)
    )
}
