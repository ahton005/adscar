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
import repo.repoNotFoundTest
import kotlin.test.assertEquals

class BizRepoReadTest {

    private val userId = InnerUserId("321")
    private val command = InnerCommand.READ
    private val initAd = InnerAd(
        id = InnerAdId("123"),
        title = "abc",
        description = "abc",
        ownerId = userId,
        visibility = InnerVisibility.VISIBLE_PUBLIC
    )
    private val repo by lazy {
        AdRepositoryMock(
            invokeReadAd = {
                DbAdResponse(
                    isSuccess = true,
                    data = initAd
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
    fun repoReadSuccessTest() = runTest {
        val ctx = InnerContext(
            command = command,
            state = InnerState.NONE,
            workMode = InnerWorkMode.TEST,
            adRequest = InnerAd(
                id = InnerAdId("123")
            )
        )
        ctx.addTestPrincipal()
        processor.exec(ctx)
        assertEquals(InnerState.FINISHING, ctx.state)
        assertEquals(initAd.id, ctx.adResponse.id)
        assertEquals(initAd.title, ctx.adResponse.title)
        assertEquals(initAd.description, ctx.adResponse.description)
        assertEquals(initAd.visibility, ctx.adResponse.visibility)
    }

    @Test
    fun repoReadNotFoundTest() = repoNotFoundTest(command)
}
