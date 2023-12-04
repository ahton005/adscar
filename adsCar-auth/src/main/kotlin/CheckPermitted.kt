import models.InnerCommand
import permissions.InnerPrincipalRelations
import permissions.InnerUserPermissions

fun checkPermitted(
    command: InnerCommand,
    relations: Iterable<InnerPrincipalRelations>,
    permissions: Iterable<InnerUserPermissions>
) =
    relations.asSequence().flatMap { relation ->
        permissions.map { permission ->
            AccessTableConditions(
                command = command,
                permission = permission,
                relation = relation
            )
        }
    }.any {
        accessTable[it] != null
    }

private data class AccessTableConditions(
    val command: InnerCommand,
    val permission: InnerUserPermissions,
    val relation: InnerPrincipalRelations
)

private val accessTable = mapOf(
    // Create
    AccessTableConditions(
        command = InnerCommand.CREATE,
        permission = InnerUserPermissions.CREATE_OWN,
        relation = InnerPrincipalRelations.NEW
    ) to true,

    // Read
    AccessTableConditions(
        command = InnerCommand.READ,
        permission = InnerUserPermissions.READ_OWN,
        relation = InnerPrincipalRelations.OWN
    ) to true,
    AccessTableConditions(
        command = InnerCommand.READ,
        permission = InnerUserPermissions.READ_PUBLIC,
        relation = InnerPrincipalRelations.PUBLIC
    ) to true,

    // Update
    AccessTableConditions(
        command = InnerCommand.UPDATE,
        permission = InnerUserPermissions.UPDATE_OWN,
        relation = InnerPrincipalRelations.OWN
    ) to true,

    // Delete
    AccessTableConditions(
        command = InnerCommand.DELETE,
        permission = InnerUserPermissions.DELETE_OWN,
        relation = InnerPrincipalRelations.OWN
    ) to true
)
