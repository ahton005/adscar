package helpers // ktlint-disable filename

import InnerContext
import models.InnerCommand.CREATE
import models.InnerCommand.DELETE
import models.InnerCommand.UPDATE

fun InnerContext.isUpdatableCommand() = this.command in listOf(CREATE, UPDATE, DELETE)
