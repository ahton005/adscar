import kotlinx.coroutines.test.runTest
import models.InnerAd
import models.InnerAdFilter
import models.InnerAdId
import models.InnerCommand
import models.InnerState
import models.InnerUserId
import models.InnerVisibility
import models.InnerWorkMode
import org.junit.Test
import repo.DbAdsResponse
import kotlin.test.assertEquals

class BizRepoSearchTest {

    private val userId = InnerUserId("321")
    private val command = InnerCommand.SEARCH
    private val initAd = InnerAd(
        id = InnerAdId("123"),
        title = "abc",
        description = "abc",
        ownerId = userId,
        visibility = InnerVisibility.VISIBLE_PUBLIC
    )
    private val repo by lazy {
        AdRepositoryMock(
            invokeSearchAd = {
                DbAdsResponse(
                    isSuccess = true,
                    data = listOf(initAd)
                )
            }
        )
    }
    private val settings by lazy {
        MkplCorSettings(
            repoTest = repo
        )
    }
    private val processor by lazy { AdProcessor(settings) }

    @Test
    fun repoSearchSuccessTest() = runTest {
        val ctx = InnerContext(
            command = command,
            state = InnerState.NONE,
            workMode = InnerWorkMode.TEST,
            adFilterRequest = InnerAdFilter(
                searchString = "ab"
            )
        )
        ctx.addTestPrincipal()
        processor.exec(ctx)
        assertEquals(InnerState.FINISHING, ctx.state)
        assertEquals(1, ctx.adsResponse.size)
    }
}
