import models.InnerAd
import models.InnerAdId
import models.InnerAdLock
import models.InnerUserId
import models.InnerVisibility

abstract class BaseInitAds(val op: String) : IInitObjects<InnerAd> {
    open val lockOld: InnerAdLock = InnerAdLock("20000000-0000-0000-0000-000000000001")
    open val lockBad: InnerAdLock = InnerAdLock("20000000-0000-0000-0000-000000000009")

    fun createInitTestModel(
        suf: String,
        ownerId: InnerUserId = InnerUserId("owner-123"),
        lock: InnerAdLock = lockOld
    ) = InnerAd(
        id = InnerAdId("ad-repo-$op-$suf"),
        title = "$suf stub",
        description = "$suf stub description",
        ownerId = ownerId,
        visibility = InnerVisibility.VISIBLE_TO_OWNER,
        lock = lock
    )
}
