package ru.zyablov.otus.otuskotlin.adscar.common.models

import java.math.BigDecimal
import java.math.BigDecimal.ZERO

data class InnerAd(
    val id: InnerAdId = InnerAdId.NONE,
    val title: String = "",
    val description: String = "",
    val visibility: InnerVisibility = InnerVisibility.NONE,
    val logos: List<String> = listOf(),
    val ownerId: InnerUserId = InnerUserId.NONE,
    val price: BigDecimal = ZERO,
    val permissionsClient: Set<InnerAdPermissionClient> = setOf()
)
