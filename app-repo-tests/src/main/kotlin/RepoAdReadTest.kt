import models.InnerAd
import models.InnerAdId
import models.InnerError
import org.junit.Assert.assertEquals
import org.junit.Test
import repo.DbAdIdRequest
import repo.IAdRepository

abstract class RepoAdReadTest {
    abstract val repo: IAdRepository
    protected open val readSuccess = initObjects[0]
    private val readIdNotFound = InnerAdId("ad-repo-read-not-found")

    @Test
    open fun readSuccess() = runRepoTest {
        val result = repo.readAd(DbAdIdRequest(readSuccess.id))

        assertEquals(true, result.isSuccess)
        assertEquals(readSuccess, result.data)
        assertEquals(emptyList<InnerError>(), result.errors)
    }

    @Test
    open fun readNotFound() = runRepoTest {
        val result = repo.readAd(DbAdIdRequest(readIdNotFound))

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)
        val error = result.errors.find { it.code == "not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitAds("read") {
        override val initObjects: List<InnerAd> = listOf(
            createInitTestModel("read"),
            createInitTestModel("readConc")
        )
    }
}
