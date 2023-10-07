package controllers

import IAppSettings
import io.ktor.server.application.ApplicationCall
import plugins.processV1
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdCreateRequest
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdCreateResponse
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdDeleteRequest
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdDeleteResponse
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdReadRequest
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdReadResponse
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdSearchRequest
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdSearchResponse
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdUpdateRequest
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdUpdateResponse

suspend fun ApplicationCall.createAd(appSettings: IAppSettings) =
    processV1<AdCreateRequest, AdCreateResponse>(appSettings)

suspend fun ApplicationCall.readAd(appSettings: IAppSettings) =
    processV1<AdReadRequest, AdReadResponse>(appSettings)

suspend fun ApplicationCall.updateAd(appSettings: IAppSettings) =
    processV1<AdUpdateRequest, AdUpdateResponse>(appSettings)

suspend fun ApplicationCall.deleteAd(appSettings: IAppSettings) =
    processV1<AdDeleteRequest, AdDeleteResponse>(appSettings)

suspend fun ApplicationCall.searchAd(appSettings: IAppSettings) =
    processV1<AdSearchRequest, AdSearchResponse>(appSettings)
