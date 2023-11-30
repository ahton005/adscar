import kotlinx.coroutines.test.runTest
import models.InnerAd
import models.InnerAdId
import models.InnerError
import org.junit.Assert.assertEquals
import org.junit.Test
import repo.DbAdIdRequest
import repo.IAdRepository

abstract class RepoAdDeleteTest {
    abstract val repo: IAdRepository

    @Test
    open fun deleteSuccess() = runRepoTest {
        val result = repo.deleteAd(DbAdIdRequest(successId, lockOld))

        assertEquals(true, result.isSuccess)
        assertEquals(emptyList<InnerError>(), result.errors)
    }

    @Test
    open fun deleteNotFound() = runRepoTest {
        val result = repo.deleteAd(DbAdIdRequest(notFoundId, lockOld))

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)
        val error = result.errors.find { it.code == "not-found" }
        assertEquals("id", error?.field)
    }

    @Test
    open fun deleteConcurrency() = runTest {
        val result = repo.deleteAd(DbAdIdRequest(concurrencyId, lock = lockBad))

        assertEquals(false, result.isSuccess)
        val error = result.errors.find { it.code == "concurrency" }
        assertEquals("lock", error?.field)
        assertEquals(lockOld, result.data?.lock)
    }

    companion object : BaseInitAds("delete") {
        override val initObjects: List<InnerAd> = listOf(
            createInitTestModel("delete"),
            createInitTestModel("deleteLock")
        )
        val successId = InnerAdId(initObjects[0].id.asString())
        val notFoundId = InnerAdId("ad-repo-delete-notFound")
        val concurrencyId = InnerAdId(initObjects[1].id.asString())
    }
}
