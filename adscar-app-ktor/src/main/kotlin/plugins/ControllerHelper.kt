package plugins // ktlint-disable filename

import IAppSettings
import base.toModel
import controllerHelper
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
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
    receive<IRequest>().toInnerContext().apply {
        principal = this@processV1.request.call.principal<JWTPrincipal>().toModel()
    },
    { respond(toTransport()) },
    clazz,
    logId
)

// Костыль для решения проблемы отсутствия jwt в native
// @Suppress("UnusedReceiverParameter", "UNUSED_PARAMETER")
// fun ApplicationCall.mkplPrincipal(appSettings: MkplAppSettings): MkplPrincipalModel = MkplPrincipalModel(
//    id = MkplUserId("user-1"),
//    fname = "Ivan",
//    mname = "Ivanovich",
//    lname = "Ivanov",
//    groups = setOf(MkplUserGroups.TEST, MkplUserGroups.USER),
// )
