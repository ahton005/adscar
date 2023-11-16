package repo

import models.InnerAd
import models.InnerAdId
import models.InnerAdLock

data class DbAdIdRequest(
    val id: InnerAdId,
    val lock: InnerAdLock = InnerAdLock.NONE
) {
    constructor(ad: InnerAd) : this(ad.id, ad.lock)
}
