import permissions.InnerUserGroups // ktlint-disable filename
import permissions.InnerUserPermissions

fun resolveChainPermissions(
    groups: Iterable<InnerUserGroups>
) = groups.flatMap { groupPermissionsAdmits[it] ?: setOf() }
    .subtract(groups.flatMap { groupPermissionsDenys[it] ?: setOf() }.toSet())

private val groupPermissionsAdmits = mapOf(
    InnerUserGroups.USER to setOf(
        InnerUserPermissions.READ_OWN,
        InnerUserPermissions.READ_PUBLIC,
        InnerUserPermissions.CREATE_OWN,
        InnerUserPermissions.UPDATE_OWN,
        InnerUserPermissions.DELETE_OWN
    ),
    InnerUserGroups.MODERATOR_MP to setOf(),
    InnerUserGroups.ADMIN_AD to setOf(),
    InnerUserGroups.TEST to setOf(),
    InnerUserGroups.BAN_AD to setOf()
)

private val groupPermissionsDenys = mapOf(
    InnerUserGroups.USER to setOf(),
    InnerUserGroups.MODERATOR_MP to setOf(),
    InnerUserGroups.ADMIN_AD to setOf(),
    InnerUserGroups.TEST to setOf(),
    InnerUserGroups.BAN_AD to setOf(
        InnerUserPermissions.UPDATE_OWN,
        InnerUserPermissions.CREATE_OWN
    )
)
