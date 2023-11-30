package model

import models.InnerAd
import models.InnerAdId
import models.InnerAdLock
import models.InnerUserId
import models.InnerVisibility

data class AdEntity(
    val id: String? = null,
    val title: String? = null,
    val description: String? = null,
    val ownerId: String? = null,
    val visibility: String? = null,
    val lock: String? = null
) {
    constructor(model: InnerAd) : this(
        id = model.id.asString().takeIf { it.isNotBlank() },
        title = model.title.takeIf { it.isNotBlank() },
        description = model.description.takeIf { it.isNotBlank() },
        ownerId = model.ownerId.asString().takeIf { it.isNotBlank() },
        visibility = model.visibility.takeIf { it != InnerVisibility.NONE }?.name,
        lock = model.lock.asString().takeIf { it.isNotBlank() }
    )

    fun toInternal() = InnerAd(
        id = id?.let { InnerAdId(it) } ?: InnerAdId.NONE,
        title = title ?: "",
        description = description ?: "",
        ownerId = ownerId?.let { InnerUserId(it) } ?: InnerUserId.NONE,
        visibility = visibility?.let { InnerVisibility.valueOf(it) } ?: InnerVisibility.NONE,
        lock = lock?.let { InnerAdLock(it) } ?: InnerAdLock.NONE
    )
}
