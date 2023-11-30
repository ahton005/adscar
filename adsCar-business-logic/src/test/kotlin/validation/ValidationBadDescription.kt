package validation

import AdProcessor
import InnerContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import models.InnerAd
import models.InnerAdLock
import models.InnerCommand
import models.InnerState
import models.InnerVisibility
import models.InnerWorkMode
import stubs.AdStub
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private val stub = AdStub.get()

@OptIn(ExperimentalCoroutinesApi::class)
fun validationDescriptionCorrect(command: InnerCommand, processor: AdProcessor) = runTest {
    val ctx = InnerContext(
        command = command,
        state = InnerState.NONE,
        workMode = InnerWorkMode.TEST,
        adRequest = InnerAd(
            id = stub.id,
            title = "abc",
            description = "abc",
            visibility = InnerVisibility.VISIBLE_PUBLIC,
            lock = InnerAdLock("123-234-abc-ABC")
        )
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(InnerState.FAILING, ctx.state)
    assertEquals("abc", ctx.adValidated.description)
}

fun validationDescriptionTrim(command: InnerCommand, processor: AdProcessor) = runTest {
    val ctx = InnerContext(
        command = command,
        state = InnerState.NONE,
        workMode = InnerWorkMode.TEST,
        adRequest = InnerAd(
            id = stub.id,
            title = "abc",
            description = " \n\tabc \n\t",
            visibility = InnerVisibility.VISIBLE_PUBLIC,
            lock = InnerAdLock("123-234-abc-ABC")
        )
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(InnerState.FAILING, ctx.state)
    assertEquals("abc", ctx.adValidated.description)
}

fun validationDescriptionEmpty(command: InnerCommand, processor: AdProcessor) = runTest {
    val ctx = InnerContext(
        command = command,
        state = InnerState.NONE,
        workMode = InnerWorkMode.TEST,
        adRequest = InnerAd(
            id = stub.id,
            title = "abc",
            description = "",
            visibility = InnerVisibility.VISIBLE_PUBLIC,
            lock = InnerAdLock("123-234-abc-ABC")
        )
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(InnerState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("description", error?.field)
    assertContains(error?.message ?: "", "description")
}

fun validationDescriptionSymbols(command: InnerCommand, processor: AdProcessor) = runTest {
    val ctx = InnerContext(
        command = command,
        state = InnerState.NONE,
        workMode = InnerWorkMode.TEST,
        adRequest = InnerAd(
            id = stub.id,
            title = "abc",
            description = "!@#$%^&*(),.{}",
            visibility = InnerVisibility.VISIBLE_PUBLIC,
            lock = InnerAdLock("123-234-abc-ABC")
        )
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(InnerState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("description", error?.field)
    assertContains(error?.message ?: "", "description")
}
