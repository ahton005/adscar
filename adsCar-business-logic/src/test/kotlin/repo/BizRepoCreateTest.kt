import kotlinx.coroutines.test.runTest
import models.InnerAd
import models.InnerAdId
import models.InnerCommand
import models.InnerState
import models.InnerUserId
import models.InnerVisibility
import models.InnerWorkMode
import org.junit.Test
import repo.DbAdResponse
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class BizRepoCreateTest {

    private val userId = InnerUserId("321")
    private val command = InnerCommand.CREATE
    private val uuid = "10000000-0000-0000-0000-000000000001"
    private val repo = AdRepositoryMock(
        invokeCreateAd = {
            DbAdResponse(
                isSuccess = true,
                data = InnerAd(
                    id = InnerAdId(uuid),
                    title = it.ad.title,
                    description = it.ad.description,
                    ownerId = userId,
                    visibility = it.ad.visibility
                )
            )
        }
    )
    private val settings = MkplCorSettings(
        repoTest = repo
    )
    private val processor = AdProcessor(settings)

    @Test
    fun repoCreateSuccessTest() = runTest {
        val ctx = InnerContext(
            command = command,
            state = InnerState.NONE,
            workMode = InnerWorkMode.TEST,
            adRequest = InnerAd(
                title = "abc",
                description = "abc",
                visibility = InnerVisibility.VISIBLE_PUBLIC
            )
        )
        processor.exec(ctx)
        assertEquals(InnerState.FINISHING, ctx.state)
        assertNotEquals(InnerAdId.NONE, ctx.adResponse.id)
        assertEquals("abc", ctx.adResponse.title)
        assertEquals("abc", ctx.adResponse.description)
        assertEquals(InnerVisibility.VISIBLE_PUBLIC, ctx.adResponse.visibility)
    }
}
