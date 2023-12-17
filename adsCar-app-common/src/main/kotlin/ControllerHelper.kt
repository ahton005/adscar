import helpers.asError
import kotlinx.datetime.Clock.System.now
import models.InnerState.FAILING
import kotlin.reflect.KClass

suspend inline fun <T> IAppSettings.controllerHelper(
    crossinline getReq: suspend () -> InnerContext,
    crossinline toResponse: suspend InnerContext.() -> T,
    clazz: KClass<*>,
    logId: String
): T {
    val logger = corSettings.loggerProvider.logger(clazz)
    var ctx = InnerContext(timeStart = now())
    return try {
        logger.doWithLogging(logId) {
            ctx = getReq().copy(timeStart = now())
            processor.exec(ctx)
            logger.info(
                msg = "Req $logId processed for ${clazz.simpleName}",
                marker = "BIZ",
                data = ctx.toLog(logId)
            )
            ctx.toResponse()
        }
    } catch (e: Throwable) {
        logger.doWithLogging("$logId-failure") {
            ctx.apply { state = FAILING; errors.add(e.asError()) }
            processor.exec(ctx)
            ctx.toResponse()
        }
    }
}
