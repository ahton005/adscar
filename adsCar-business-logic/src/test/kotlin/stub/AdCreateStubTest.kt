package stub

import AdProcessor
import InnerContext
import kotlinx.coroutines.test.runTest
import models.InnerAd
import models.InnerAdId
import models.InnerCommand
import models.InnerState
import models.InnerVisibility
import models.InnerWorkMode
import stubs.AdStub
import stubs.InnerStubs
import kotlin.test.Test
import kotlin.test.assertEquals

class AdCreateStubTest {

    private val processor = AdProcessor()
    val id = InnerAdId("666")
    val title = "title 666"
    val description = "desc 666"
    val visibility = InnerVisibility.VISIBLE_PUBLIC

    @Test
    fun create() = runTest {
        val ctx = InnerContext(
            command = InnerCommand.CREATE,
            state = InnerState.NONE,
            workMode = InnerWorkMode.STUB,
            stubCase = InnerStubs.SUCCESS,
            adRequest = InnerAd(
                id = id,
                title = title,
                description = description,
                visibility = visibility
            )
        )
        processor.exec(ctx)
        assertEquals(AdStub.get().id, ctx.adResponse.id)
        assertEquals(title, ctx.adResponse.title)
        assertEquals(description, ctx.adResponse.description)
        assertEquals(visibility, ctx.adResponse.visibility)
    }

    @Test
    fun badTitle() = runTest {
        val ctx = InnerContext(
            command = InnerCommand.CREATE,
            state = InnerState.NONE,
            workMode = InnerWorkMode.STUB,
            stubCase = InnerStubs.BAD_TITLE,
            adRequest = InnerAd(
                id = id,
                title = "",
                description = description,
                visibility = visibility
            )
        )
        processor.exec(ctx)
        assertEquals(InnerAd(), ctx.adResponse)
        assertEquals("title", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badDescription() = runTest {
        val ctx = InnerContext(
            command = InnerCommand.CREATE,
            state = InnerState.NONE,
            workMode = InnerWorkMode.STUB,
            stubCase = InnerStubs.BAD_DESCRIPTION,
            adRequest = InnerAd(
                id = id,
                title = title,
                description = "",
                visibility = visibility
            )
        )
        processor.exec(ctx)
        assertEquals(InnerAd(), ctx.adResponse)
        assertEquals("description", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = InnerContext(
            command = InnerCommand.CREATE,
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
            command = InnerCommand.CREATE,
            state = InnerState.NONE,
            workMode = InnerWorkMode.STUB,
            stubCase = InnerStubs.BAD_ID,
            adRequest = InnerAd(
                id = id,
                title = title,
                description = description,
                visibility = visibility
            )
        )
        processor.exec(ctx)
        assertEquals(InnerAd(), ctx.adResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
}
