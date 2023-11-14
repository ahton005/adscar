package stub

import AdProcessor
import InnerContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import models.InnerAd
import models.InnerAdId
import models.InnerCommand
import models.InnerState
import models.InnerWorkMode
import stubs.AdStub
import stubs.InnerStubs
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class AdReadStubTest {

    private val processor = AdProcessor()
    val id = InnerAdId("666")

    @Test
    fun read() = runTest {
        val ctx = InnerContext(
            command = InnerCommand.READ,
            state = InnerState.NONE,
            workMode = InnerWorkMode.STUB,
            stubCase = InnerStubs.SUCCESS,
            adRequest = InnerAd(
                id = id
            )
        )
        processor.exec(ctx)
        with(AdStub.get()) {
            assertEquals(id, ctx.adResponse.id)
            assertEquals(title, ctx.adResponse.title)
            assertEquals(description, ctx.adResponse.description)
            assertEquals(visibility, ctx.adResponse.visibility)
        }
    }

    @Test
    fun badId() = runTest {
        val ctx = InnerContext(
            command = InnerCommand.READ,
            state = InnerState.NONE,
            workMode = InnerWorkMode.STUB,
            stubCase = InnerStubs.BAD_ID,
            adRequest = InnerAd()
        )
        processor.exec(ctx)
        assertEquals(InnerAd(), ctx.adResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = InnerContext(
            command = InnerCommand.READ,
            state = InnerState.NONE,
            workMode = InnerWorkMode.STUB,
            stubCase = InnerStubs.DB_ERROR,
            adRequest = InnerAd(
                id = id
            )
        )
        processor.exec(ctx)
        assertEquals(InnerAd(), ctx.adResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = InnerContext(
            command = InnerCommand.READ,
            state = InnerState.NONE,
            workMode = InnerWorkMode.STUB,
            stubCase = InnerStubs.BAD_TITLE,
            adRequest = InnerAd(
                id = id
            )
        )
        processor.exec(ctx)
        assertEquals(InnerAd(), ctx.adResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}
