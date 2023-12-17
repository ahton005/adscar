package validation

import AdProcessor
import AdRepoStub
import InnerContext
import MkplCorSettings
import addTestPrincipal
import kotlinx.coroutines.test.runTest
import models.InnerAdFilter
import models.InnerCommand
import models.InnerState
import models.InnerWorkMode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class BizValidationSearchTest {

    private val command = InnerCommand.SEARCH
    private val settings by lazy {
        MkplCorSettings(
            repoTest = AdRepoStub()
        )
    }
    private val processor by lazy { AdProcessor(settings) }

    @Test
    fun correctEmpty() = runTest {
        val ctx = InnerContext(
            command = command,
            state = InnerState.NONE,
            workMode = InnerWorkMode.TEST,
            adFilterRequest = InnerAdFilter()
        )
        ctx.addTestPrincipal()
        processor.exec(ctx)
        assertEquals(0, ctx.errors.size)
        assertNotEquals(InnerState.FAILING, ctx.state)
    }
}
