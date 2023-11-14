package logging // ktlint-disable filename

import ch.qos.logback.classic.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

/**
 * Generate internal MpLogContext logger
 *
 * @param logger Logback instance from [LoggerFactory.getLogger()]
 */
fun mpLoggerJvm(logger: Logger): LogWrapper = LogWrapperImpl(
    logger = logger,
    loggerId = logger.name
)

fun mpLoggerJvm(clazz: KClass<*>): LogWrapper = mpLoggerJvm(LoggerFactory.getLogger(clazz.java) as Logger)

@Suppress("unused")
fun mpLoggerJvm(loggerId: String): LogWrapper = mpLoggerJvm(LoggerFactory.getLogger(loggerId) as Logger)
