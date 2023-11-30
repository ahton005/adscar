package model

import models.InnerVisibility

enum class AdVisibility {
    VISIBLE_TO_OWNER,
    VISIBLE_TO_GROUP,
    VISIBLE_PUBLIC,
}

fun AdVisibility?.fromTransport() = when (this) {
    null -> InnerVisibility.NONE
    AdVisibility.VISIBLE_TO_OWNER -> InnerVisibility.VISIBLE_TO_OWNER
    AdVisibility.VISIBLE_TO_GROUP -> InnerVisibility.VISIBLE_TO_GROUP
    AdVisibility.VISIBLE_PUBLIC -> InnerVisibility.VISIBLE_PUBLIC
}

fun InnerVisibility.toTransport() = when (this) {
    InnerVisibility.NONE -> null
    InnerVisibility.VISIBLE_TO_OWNER -> AdVisibility.VISIBLE_TO_OWNER
    InnerVisibility.VISIBLE_TO_GROUP -> AdVisibility.VISIBLE_TO_GROUP
    InnerVisibility.VISIBLE_PUBLIC -> AdVisibility.VISIBLE_PUBLIC
}
