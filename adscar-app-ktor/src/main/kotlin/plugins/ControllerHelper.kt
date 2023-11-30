package plugins // ktlint-disable filename

import IAppSettings
import controllerHelper
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import mappers.v1.toInnerContext
import mappers.v1.toTransport
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.IRequest
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.IResponse
import kotlin.reflect.KClass

suspend inline fun <reified Q : IRequest,
    @Suppress("unused")
    reified R : IResponse> ApplicationCall.processV1(
    appSettings: IAppSettings,
    clazz: KClass<*>,
    logId: String
) = appSettings.controllerHelper(
    receive<IRequest>().toInnerContext(),
    { respond(toTransport()) },
    clazz,
    logId
)
