import models.InnerAdPermissionClient // ktlint-disable filename
import permissions.InnerPrincipalRelations
import permissions.InnerUserPermissions

fun resolveFrontPermissions(
    permissions: Iterable<InnerUserPermissions>,
    relations: Iterable<InnerPrincipalRelations>
) = mutableSetOf<InnerAdPermissionClient>()
    .apply {
        for (permission in permissions) {
            for (relation in relations) {
                accessTable[permission]?.get(relation)?.let { this@apply.add(it) }
            }
        }
    }
    .toSet()

private val accessTable = mapOf(
    // READ
    InnerUserPermissions.READ_OWN to mapOf(
        InnerPrincipalRelations.OWN to InnerAdPermissionClient.READ
    ),
    InnerUserPermissions.READ_GROUP to mapOf(
        InnerPrincipalRelations.GROUP to InnerAdPermissionClient.READ
    ),
    InnerUserPermissions.READ_PUBLIC to mapOf(
        InnerPrincipalRelations.PUBLIC to InnerAdPermissionClient.READ
    ),
    InnerUserPermissions.READ_CANDIDATE to mapOf(
        InnerPrincipalRelations.MODERATABLE to InnerAdPermissionClient.READ
    ),

    // UPDATE
    InnerUserPermissions.UPDATE_OWN to mapOf(
        InnerPrincipalRelations.OWN to InnerAdPermissionClient.UPDATE
    ),
    InnerUserPermissions.UPDATE_PUBLIC to mapOf(
        InnerPrincipalRelations.MODERATABLE to InnerAdPermissionClient.UPDATE
    ),
    InnerUserPermissions.UPDATE_CANDIDATE to mapOf(
        InnerPrincipalRelations.MODERATABLE to InnerAdPermissionClient.UPDATE
    ),

    // DELETE
    InnerUserPermissions.DELETE_OWN to mapOf(
        InnerPrincipalRelations.OWN to InnerAdPermissionClient.DELETE
    ),
    InnerUserPermissions.DELETE_PUBLIC to mapOf(
        InnerPrincipalRelations.MODERATABLE to InnerAdPermissionClient.DELETE
    ),
    InnerUserPermissions.DELETE_CANDIDATE to mapOf(
        InnerPrincipalRelations.MODERATABLE to InnerAdPermissionClient.DELETE
    )
)
