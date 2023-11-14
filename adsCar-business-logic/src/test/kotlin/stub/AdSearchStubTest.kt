package stub

import AdProcessor
import InnerContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import models.InnerAd
import models.InnerAdFilter
import models.InnerCommand
import models.InnerState
import models.InnerWorkMode
import stubs.AdStub
import stubs.InnerStubs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

@OptIn(ExperimentalCoroutinesApi::class)
class AdSearchStubTest {

    private val processor = AdProcessor()
    val filter = InnerAdFilter(searchString = "ГАЗ")

    @Test
    fun read() = runTest {
        val ctx = InnerContext(
            command = InnerCommand.SEARCH,
            state = InnerState.NONE,
            workMode = InnerWorkMode.STUB,
            stubCase = InnerStubs.SUCCESS,
            adFilterRequest = filter
        )
        processor.exec(ctx)
        assertTrue(ctx.adsResponse.size > 1)
        val first = ctx.adsResponse.firstOrNull() ?: fail("Empty response list")
        assertTrue(first.description.contains(filter.searchString))
        with(AdStub.get()) {
            assertEquals(visibility, first.visibility)
        }
    }

    @Test
    fun badId() = runTest {
        val ctx = InnerContext(
            command = InnerCommand.SEARCH,
            state = InnerState.NONE,
            workMode = InnerWorkMode.STUB,
            stubCase = InnerStubs.BAD_ID,
            adFilterRequest = filter
        )
        processor.exec(ctx)
        assertEquals(InnerAd(), ctx.adResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = InnerContext(
            command = InnerCommand.SEARCH,
            state = InnerState.NONE,
            workMode = InnerWorkMode.STUB,
            stubCase = InnerStubs.DB_ERROR,
            adFilterRequest = filter
        )
        processor.exec(ctx)
        assertEquals(InnerAd(), ctx.adResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = InnerContext(
            command = InnerCommand.SEARCH,
            state = InnerState.NONE,
            workMode = InnerWorkMode.STUB,
            stubCase = InnerStubs.BAD_TITLE,
            adFilterRequest = filter
        )
        processor.exec(ctx)
        assertEquals(InnerAd(), ctx.adResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}
