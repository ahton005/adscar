package validation

import AdProcessor
import InnerContext
import addTestPrincipal
import kotlinx.coroutines.test.runTest
import models.InnerAd
import models.InnerAdId
import models.InnerAdLock
import models.InnerCommand
import models.InnerState
import models.InnerVisibility
import models.InnerWorkMode
import stubs.AdStub
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private val stub = AdStub.prepareResult { id = InnerAdId("123-234-abc-ABC") }

fun validationIdCorrect(command: InnerCommand, processor: AdProcessor) = runTest {
    val ctx = InnerContext(
        command = command,
        state = InnerState.NONE,
        workMode = InnerWorkMode.TEST,
        adRequest = InnerAd(
            id = InnerAdId("123-234-abc-ABC"),
            title = "abc",
            description = "abc",
            visibility = InnerVisibility.VISIBLE_PUBLIC,
            lock = InnerAdLock("123-234-abc-ABC")
        )
    )
    ctx.addTestPrincipal(stub.ownerId)
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(InnerState.FAILING, ctx.state)
}

fun validationIdTrim(command: InnerCommand, processor: AdProcessor) = runTest {
    val ctx = InnerContext(
        command = command,
        state = InnerState.NONE,
        workMode = InnerWorkMode.TEST,
        adRequest = InnerAd(
            id = InnerAdId(" \n\t 123-234-abc-ABC \n\t "),
            title = "abc",
            description = "abc",
            visibility = InnerVisibility.VISIBLE_PUBLIC,
            lock = InnerAdLock("123-234-abc-ABC")
        )
    )
    ctx.addTestPrincipal(stub.ownerId)
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(InnerState.FAILING, ctx.state)
}

fun validationIdEmpty(command: InnerCommand, processor: AdProcessor) = runTest {
    val ctx = InnerContext(
        command = command,
        state = InnerState.NONE,
        workMode = InnerWorkMode.TEST,
        adRequest = InnerAd(
            id = InnerAdId(""),
            title = "abc",
            description = "abc",
            visibility = InnerVisibility.VISIBLE_PUBLIC,
            lock = InnerAdLock("123-234-abc-ABC")
        )
    )
    ctx.addTestPrincipal(stub.ownerId)
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(InnerState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
}

fun validationIdFormat(command: InnerCommand, processor: AdProcessor) = runTest {
    val ctx = InnerContext(
        command = command,
        state = InnerState.NONE,
        workMode = InnerWorkMode.TEST,
        adRequest = InnerAd(
            id = InnerAdId("!@#\$%^&*(),.{}"),
            title = "abc",
            description = "abc",
            visibility = InnerVisibility.VISIBLE_PUBLIC,
            lock = InnerAdLock("123-234-abc-ABC")
        )
    )
    ctx.addTestPrincipal(stub.ownerId)
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(InnerState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
}
