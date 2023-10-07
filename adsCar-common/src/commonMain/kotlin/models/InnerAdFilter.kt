package models

import java.math.BigDecimal
import java.math.BigDecimal.ZERO

data class InnerAdFilter(
    val searchString: String = "",
    val ownerId: InnerUserId = InnerUserId.NONE,
    val price: BigDecimal = ZERO
)
