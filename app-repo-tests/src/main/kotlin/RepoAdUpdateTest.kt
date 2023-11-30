import kotlinx.coroutines.test.runTest
import models.InnerAd
import models.InnerAdId
import models.InnerAdLock
import models.InnerError
import models.InnerUserId
import models.InnerVisibility.VISIBLE_TO_GROUP
import org.junit.Assert.assertEquals
import org.junit.Test
import repo.DbAdRequest
import repo.IAdRepository

abstract class RepoAdUpdateTest {
    abstract val repo: IAdRepository
    protected open val updateSuccess = initObjects[0]
    protected val updateConc = initObjects[1]
    private val updateIdNotFound = InnerAdId("ad-repo-update-not-found")

    protected val lockBad = InnerAdLock("20000000-0000-0000-0000-000000000009")
    protected val lockNew = InnerAdLock("20000000-0000-0000-0000-000000000002")

    private val reqUpdateSucc by lazy {
        InnerAd(
            id = updateSuccess.id,
            title = "update object",
            description = "update object description",
            ownerId = InnerUserId("owner-123"),
            visibility = VISIBLE_TO_GROUP,
            lock = initObjects.first().lock
        )
    }
    private val reqUpdateNotFound = InnerAd(
        id = updateIdNotFound,
        title = "update object not found",
        description = "update object not found description",
        ownerId = InnerUserId("owner-123"),
        visibility = VISIBLE_TO_GROUP,
        lock = initObjects.first().lock
    )

    private val reqUpdateConc = InnerAd(
        id = updateConc.id,
        title = "update object not found",
        description = "update object not found description",
        ownerId = InnerUserId("owner-123"),
        visibility = VISIBLE_TO_GROUP,
        lock = lockBad
    )

    @Test
    open fun updateSuccess() = runRepoTest {
        val result = repo.updateAd(DbAdRequest(reqUpdateSucc))
        assertEquals(true, result.isSuccess)
        assertEquals(reqUpdateSucc.id, result.data?.id)
        assertEquals(reqUpdateSucc.title, result.data?.title)
        assertEquals(reqUpdateSucc.description, result.data?.description)
        assertEquals(emptyList<InnerError>(), result.errors)
    }

    @Test
    open fun updateNotFound() = runRepoTest {
        val result = repo.updateAd(DbAdRequest(reqUpdateNotFound))
        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)
        val error = result.errors.find { it.code == "not-found" }
        assertEquals("id", error?.field)
    }

    @Test
    open fun updateConcurrencyError() = runTest {
        val result = repo.updateAd(DbAdRequest(reqUpdateConc))
        assertEquals(false, result.isSuccess)
        val error = result.errors.find { it.code == "concurrency" }
        assertEquals("lock", error?.field)
        assertEquals(updateConc, result.data)
    }

    companion object : BaseInitAds("update") {
        override val initObjects: List<InnerAd> = listOf(
            createInitTestModel("update"),
            createInitTestModel("updateConc")
        )
    }
}
