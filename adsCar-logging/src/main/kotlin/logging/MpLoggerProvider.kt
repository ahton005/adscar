package logging

import logging.LogWrapper.Companion.DEFAULT
import kotlin.reflect.KClass

class MpLoggerProvider(
    private val provider: (String) -> LogWrapper = { DEFAULT }
) {
    fun logger(loggerId: String) = provider(loggerId)
    fun logger(clazz: KClass<*>) = provider(clazz.qualifiedName ?: clazz.simpleName ?: "(unknown)")
}
