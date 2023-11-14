import helpers.asError
import kotlinx.datetime.Clock.System.now
import models.InnerState.FAILING
import kotlin.reflect.KClass

suspend inline fun <T> IAppSettings.controllerHelper(
    ctx: InnerContext,
    crossinline toResponse: suspend InnerContext.() -> T,
    clazz: KClass<*>,
    logId: String
): T {
    val logger = corSettings.loggerProvider.logger(clazz)
    return try {
        logger.doWithLogging(logId) {
            processor.exec(ctx.apply { timeStart = now() })
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
