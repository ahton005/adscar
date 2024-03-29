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

fun validationLockCorrect(command: InnerCommand, processor: AdProcessor) = runTest {
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

fun validationLockTrim(command: InnerCommand, processor: AdProcessor) = runTest {
    val ctx = InnerContext(
        command = command,
        state = InnerState.NONE,
        workMode = InnerWorkMode.TEST,
        adRequest = InnerAd(
            id = InnerAdId("123-234-abc-ABC"),
            title = "abc",
            description = "abc",
            visibility = InnerVisibility.VISIBLE_PUBLIC,
            lock = InnerAdLock(" \n\t 123-234-abc-ABC \n\t ")
        )
    )
    ctx.addTestPrincipal(stub.ownerId)
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(InnerState.FAILING, ctx.state)
}

fun validationLockEmpty(command: InnerCommand, processor: AdProcessor) = runTest {
    val ctx = InnerContext(
        command = command,
        state = InnerState.NONE,
        workMode = InnerWorkMode.TEST,
        adRequest = InnerAd(
            id = InnerAdId("123-234-abc-ABC"),
            title = "abc",
            description = "abc",
            visibility = InnerVisibility.VISIBLE_PUBLIC,
            lock = InnerAdLock("")
        )
    )
    ctx.addTestPrincipal()
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(InnerState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("lock", error?.field)
    assertContains(error?.message ?: "", "id")
}

fun validationLockFormat(command: InnerCommand, processor: AdProcessor) = runTest {
    val ctx = InnerContext(
        command = command,
        state = InnerState.NONE,
        workMode = InnerWorkMode.TEST,
        adRequest = InnerAd(
            id = InnerAdId("123-234-abc-ABC"),
            title = "abc",
            description = "abc",
            visibility = InnerVisibility.VISIBLE_PUBLIC,
            lock = InnerAdLock("!@#\$%^&*(),.{}")
        )
    )
    ctx.addTestPrincipal()
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(InnerState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("lock", error?.field)
    assertContains(error?.message ?: "", "id")
}
