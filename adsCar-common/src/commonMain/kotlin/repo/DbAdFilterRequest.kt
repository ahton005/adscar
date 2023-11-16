package repo

import models.InnerUserId
import models.InnerUserId.Companion.NONE

data class DbAdFilterRequest(
    val titleFilter: String = "",
    val ownerId: InnerUserId = NONE
)
