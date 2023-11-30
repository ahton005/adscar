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

class BizRepoUpdateTest {

    private val userId = InnerUserId("321")
    private val command = InnerCommand.UPDATE
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
            },
            invokeUpdateAd = {
                DbAdResponse(
                    isSuccess = true,
                    data = InnerAd(
                        id = InnerAdId("123"),
                        title = "xyz",
                        description = "xyz",
                        visibility = InnerVisibility.VISIBLE_TO_GROUP
                    )
                )
            }
        )
    }
    private val settings by lazy { MkplCorSettings(repoTest = repo) }
    private val processor by lazy { AdProcessor(settings) }

    @Test
    fun repoUpdateSuccessTest() = runTest {
        val adToUpdate = InnerAd(
            id = InnerAdId("123"),
            title = "xyz",
            description = "xyz",
            visibility = InnerVisibility.VISIBLE_TO_GROUP,
            lock = InnerAdLock("123-234-abc-ABC")
        )
        val ctx = InnerContext(
            command = command,
            state = InnerState.NONE,
            workMode = InnerWorkMode.TEST,
            adRequest = adToUpdate
        )
        processor.exec(ctx)
        assertEquals(InnerState.FINISHING, ctx.state)
        assertEquals(adToUpdate.id, ctx.adResponse.id)
        assertEquals(adToUpdate.title, ctx.adResponse.title)
        assertEquals(adToUpdate.description, ctx.adResponse.description)
        assertEquals(adToUpdate.visibility, ctx.adResponse.visibility)
    }

    @Test
    fun repoUpdateNotFoundTest() = repoNotFoundTest(command)
}
