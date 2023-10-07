import helpers.asError
import kotlinx.datetime.Clock.System.now
import models.InnerState.FAILING

suspend inline fun <T> IAppSettings.controllerHelper(
    ctx: InnerContext,
    toResponse: InnerContext.() -> T
): T {
    return try {
        processor.exec(ctx.copy(timeStart = now())).toResponse()
    } catch (e: Throwable) {
        val newCtx = ctx.copy(state = FAILING, errors = ctx.errors + e.asError())
        processor.exec(newCtx).toResponse()
    }
}
