import models.InnerAd
import models.InnerAdId
import models.InnerAdLock
import models.InnerError
import models.InnerUserId
import models.InnerVisibility
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import repo.DbAdRequest
import repo.IAdRepository

abstract class RepoAdCreateTest {
    protected open val lockNew: InnerAdLock = InnerAdLock("20000000-0000-0000-0000-000000000002")

    abstract val repo: IAdRepository

    private val createObj = InnerAd(
        title = "create object",
        description = "create object description",
        ownerId = InnerUserId("owner-123"),
        visibility = InnerVisibility.VISIBLE_TO_GROUP
    )

    @Test
    open fun createSuccess() = runRepoTest {
        val result = repo.createAd(DbAdRequest(createObj))
        val expected = createObj.copy(id = result.data?.id ?: InnerAdId.NONE)
        assertEquals(true, result.isSuccess)
        assertEquals(expected.title, result.data?.title)
        assertEquals(expected.description, result.data?.description)
        assertNotEquals(InnerAdId.NONE, result.data?.id)
        assertEquals(listOf<InnerError>(), result.errors)
    }

    companion object : BaseInitAds("create") {
        override val initObjects: List<InnerAd> = listOf()
    }
}
