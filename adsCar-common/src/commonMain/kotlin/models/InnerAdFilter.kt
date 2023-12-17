package models

import java.math.BigDecimal
import java.math.BigDecimal.ZERO

data class InnerAdFilter(
    var searchString: String = "",
    var ownerId: InnerUserId = InnerUserId.NONE,
    var price: BigDecimal = ZERO,
    var searchPermissions: MutableSet<InnerSearchPermissions> = mutableSetOf()
)
