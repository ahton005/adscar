package repo // ktlint-disable filename

import AdProcessor
import AdRepositoryMock
import InnerContext
import MkplCorSettings
import addTestPrincipal
import kotlinx.coroutines.test.runTest
import models.InnerAd
import models.InnerAdId
import models.InnerAdLock
import models.InnerCommand
import models.InnerError
import models.InnerState
import models.InnerVisibility
import models.InnerWorkMode
import kotlin.test.assertEquals

private val initAd = InnerAd(
    id = InnerAdId("123"),
    title = "abc",
    description = "abc",
    visibility = InnerVisibility.VISIBLE_PUBLIC
)
private val repo = AdRepositoryMock(
    invokeReadAd = {
        if (it.id == initAd.id) {
            DbAdResponse(
                isSuccess = true,
                data = initAd
            )
        } else DbAdResponse(
            isSuccess = false,
            data = null,
            errors = listOf(InnerError(message = "Not found", field = "id"))
        )
    }
)
private val settings by lazy {
    MkplCorSettings(
        repoTest = repo
    )
}
private val processor by lazy { AdProcessor(settings) }

fun repoNotFoundTest(command: InnerCommand) = runTest {
    val ctx = InnerContext(
        command = command,
        state = InnerState.NONE,
        workMode = InnerWorkMode.TEST,
        adRequest = InnerAd(
            id = InnerAdId("12345"),
            title = "xyz",
            description = "xyz",
            visibility = InnerVisibility.VISIBLE_TO_GROUP,
            lock = InnerAdLock("123-234-abc-ABC")
        )
    )
    ctx.addTestPrincipal()
    processor.exec(ctx)
    assertEquals(InnerState.FAILING, ctx.state)
    assertEquals(InnerAd(), ctx.adResponse)
    assertEquals(1, ctx.errors.size)
    assertEquals("id", ctx.errors.first().field)
}
