import models.InnerAd
import models.InnerError
import models.InnerUserId
import org.junit.Assert.assertEquals
import org.junit.Test
import repo.DbAdFilterRequest
import repo.IAdRepository

abstract class RepoAdSearchTest {
    abstract val repo: IAdRepository

    protected open val initializedObjects: List<InnerAd> = initObjects

    @Test
    open fun searchOwner() = runRepoTest {
        val result = repo.searchAd(DbAdFilterRequest(ownerId = searchOwnerId))

        assertEquals(true, result.isSuccess)
        val expected = listOf(initializedObjects[1], initializedObjects[3]).sortedBy { it.id.asString() }
        assertEquals(expected, result.data?.sortedBy { it.id.asString() })
        assertEquals(emptyList<InnerError>(), result.errors)
    }

    companion object : BaseInitAds("search") {

        val searchOwnerId = InnerUserId("owner-124")
        override val initObjects: List<InnerAd> = listOf(
            createInitTestModel("ad1"),
            createInitTestModel("ad2", ownerId = searchOwnerId),
            createInitTestModel("ad3"),
            createInitTestModel("ad4", ownerId = searchOwnerId),
            createInitTestModel("ad5")
        )
    }
}
