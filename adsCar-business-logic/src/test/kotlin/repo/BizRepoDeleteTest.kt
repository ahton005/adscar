import kotlinx.coroutines.test.runTest
import models.InnerAd
import models.InnerAdId
import models.InnerAdLock
import models.InnerCommand
import models.InnerState
import models.InnerUserId
import models.InnerVisibility
import models.InnerWorkMode
import org.junit.Test
import repo.DbAdResponse
import repo.repoNotFoundTest
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BizRepoDeleteTest {

    private val userId = InnerUserId("321")
    private val command = InnerCommand.DELETE
    private val initAd = InnerAd(
        id = InnerAdId("123"),
        title = "abc",
        description = "abc",
        ownerId = userId,
        visibility = InnerVisibility.VISIBLE_PUBLIC,
        lock = InnerAdLock("123-234-abc-ABC")
    )
    private val repo by lazy {
        AdRepositoryMock(
            invokeReadAd = {
                DbAdResponse(isSuccess = true, data = initAd)
            },
            invokeDeleteAd = {
                if (it.id == initAd.id) {
                    DbAdResponse(isSuccess = true, data = initAd)
                } else DbAdResponse(isSuccess = false, data = null)
            }
        )
    }
    private val settings by lazy {
        MkplCorSettings(repoTest = repo)
    }
    private val processor by lazy { AdProcessor(settings) }

    @Test
    fun repoDeleteSuccessTest() = runTest {
        val adToUpdate = InnerAd(
            id = InnerAdId("123"),
            lock = InnerAdLock("123-234-abc-ABC")
        )
        val ctx = InnerContext(
            command = command,
            state = InnerState.NONE,
            workMode = InnerWorkMode.TEST,
            adRequest = adToUpdate
        )
        ctx.addTestPrincipal()
        processor.exec(ctx)
        assertEquals(InnerState.FINISHING, ctx.state)
        assertTrue { ctx.errors.isEmpty() }
        assertEquals(initAd.id, ctx.adResponse.id)
        assertEquals(initAd.title, ctx.adResponse.title)
        assertEquals(initAd.description, ctx.adResponse.description)
        assertEquals(initAd.visibility, ctx.adResponse.visibility)
    }

    @Test
    fun repoDeleteNotFoundTest() = repoNotFoundTest(command)
}
