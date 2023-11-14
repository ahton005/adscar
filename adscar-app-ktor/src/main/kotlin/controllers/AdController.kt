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
import kotlin.reflect.KClass

private val clCreate: KClass<*> = ApplicationCall::createAd::class
suspend fun ApplicationCall.createAd(appSettings: IAppSettings) =
    processV1<AdCreateRequest, AdCreateResponse>(appSettings, clCreate, "create")

private val clRead: KClass<*> = ApplicationCall::readAd::class
suspend fun ApplicationCall.readAd(appSettings: IAppSettings) =
    processV1<AdReadRequest, AdReadResponse>(appSettings, clRead, "read")

private val clUpdate: KClass<*> = ApplicationCall::updateAd::class
suspend fun ApplicationCall.updateAd(appSettings: IAppSettings) =
    processV1<AdUpdateRequest, AdUpdateResponse>(appSettings, clUpdate, "update")

private val clDelete: KClass<*> = ApplicationCall::deleteAd::class
suspend fun ApplicationCall.deleteAd(appSettings: IAppSettings) =
    processV1<AdDeleteRequest, AdDeleteResponse>(appSettings, clDelete, "delete")

private val clSearch: KClass<*> = ApplicationCall::searchAd::class
suspend fun ApplicationCall.searchAd(appSettings: IAppSettings) =
    processV1<AdSearchRequest, AdSearchResponse>(appSettings, clSearch, "search")
