package plugins // ktlint-disable filename

import io.ktor.server.application.Application
import logging.MpLoggerProvider
import logging.mpLoggerJvm

fun Application.getLoggerProviderConf(): MpLoggerProvider =
    when (val mode = environment.config.propertyOrNull("ktor.logger")?.getString()) {
        "logback", null -> MpLoggerProvider { mpLoggerJvm(it) }
        else -> throw Exception("Logger $mode is not allowed. Additted values are kmp and logback")
    }
