package controllers

import AdProcessor
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdCreateRequest
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdDeleteRequest
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdReadRequest
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdSearchRequest
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdUpdateRequest
import ru.zyablov.otus.otuskotlin.adscar.mappers.v1.toInnerContext
import ru.zyablov.otus.otuskotlin.adscar.mappers.v1.toTransport

suspend fun ApplicationCall.createAd(processor: AdProcessor) {
    val request = receive<AdCreateRequest>()
    val context = processor.exec(request.toInnerContext())
    respond(context.toTransport())
}

suspend fun ApplicationCall.readAd(processor: AdProcessor) {
    val request = receive<AdReadRequest>()
    val context = processor.exec(request.toInnerContext())
    respond(context.toTransport())
}

suspend fun ApplicationCall.updateAd(processor: AdProcessor) {
    val request = receive<AdUpdateRequest>()
    val context = processor.exec(request.toInnerContext())
    respond(context.toTransport())
}

suspend fun ApplicationCall.deleteAd(processor: AdProcessor) {
    val request = receive<AdDeleteRequest>()
    val context = processor.exec(request.toInnerContext())
    respond(context.toTransport())
}

suspend fun ApplicationCall.searchAd(processor: AdProcessor) {
    val request = receive<AdSearchRequest>()
    val context = processor.exec(request.toInnerContext())
    respond(context.toTransport())
}
