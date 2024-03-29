package mappers.v1

import InnerContext
import mappers.v1.exceptions.UnknownRequestClass
import models.InnerAd
import models.InnerAdFilter
import models.InnerAdId
import models.InnerAdLock
import models.InnerCommand.CREATE
import models.InnerCommand.DELETE
import models.InnerCommand.READ
import models.InnerCommand.SEARCH
import models.InnerCommand.UPDATE
import models.InnerRequestId
import models.InnerVisibility
import models.InnerWorkMode
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdCreateOrUpdateObject
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdCreateRequest
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdDeleteObject
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdDeleteRequest
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdReadObject
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdReadRequest
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdRequestDebug
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdRequestDebugMode
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdRequestDebugStubs
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdSearchFilter
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdSearchRequest
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdUpdateRequest
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdVisibility
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.IRequest
import stubs.InnerStubs
import java.math.BigDecimal.ZERO

fun IRequest.toInnerContext() = when (this) {
    is AdCreateRequest -> toInner()
    is AdReadRequest -> toInner()
    is AdUpdateRequest -> toInner()
    is AdDeleteRequest -> toInner()
    is AdSearchRequest -> toInner()
    else -> throw UnknownRequestClass(javaClass)
}

private fun AdRequestDebug?.toInnerMode() = when (this?.mode) {
    AdRequestDebugMode.PROD -> InnerWorkMode.PROD
    AdRequestDebugMode.TEST -> InnerWorkMode.TEST
    AdRequestDebugMode.STUB -> InnerWorkMode.STUB
    null -> InnerWorkMode.PROD
}

private fun AdRequestDebug?.toInnerStub() = when (this?.stub) {
    AdRequestDebugStubs.SUCCESS -> InnerStubs.SUCCESS
    AdRequestDebugStubs.NOT_FOUND -> InnerStubs.NOT_FOUND
    AdRequestDebugStubs.BAD_ID -> InnerStubs.BAD_ID
    AdRequestDebugStubs.BAD_TITLE -> InnerStubs.BAD_TITLE
    AdRequestDebugStubs.BAD_DESCRIPTION -> InnerStubs.BAD_DESCRIPTION
    AdRequestDebugStubs.BAD_VISIBILITY -> InnerStubs.BAD_VISIBILITY
    AdRequestDebugStubs.CANNOT_DELETE -> InnerStubs.CANNOT_DELETE
    AdRequestDebugStubs.BAD_SEARCH_STRING -> InnerStubs.BAD_SEARCH_STRING
    null -> InnerStubs.NONE
}

private fun AdVisibility?.toInner() = when (this) {
    AdVisibility.PUBLIC -> InnerVisibility.VISIBLE_PUBLIC
    AdVisibility.OWNER_ONLY -> InnerVisibility.VISIBLE_TO_OWNER
    AdVisibility.REGISTERED_ONLY -> InnerVisibility.VISIBLE_TO_GROUP
    null -> InnerVisibility.NONE
}

private fun AdDeleteObject?.toInner() = InnerAd(id = this?.id.toAdId(), lock = this?.lock.toInnerLock())
private fun AdReadObject?.toInner() = InnerAd(id = this?.id.toAdId())

private fun String?.toAdId() = this?.let { InnerAdId(it) } ?: InnerAdId.NONE

private fun String?.toInnerLock() = this?.let { InnerAdLock(it) } ?: InnerAdLock.NONE

private fun AdCreateOrUpdateObject?.toInnerAd() = InnerAd(
    id = this?.id?.let { InnerAdId(it) } ?: InnerAdId.NONE,
    title = this?.title ?: "",
    description = this?.description ?: "",
    logos = this?.logos ?: listOf(),
    visibility = this?.visibility.toInner(),
    price = this?.price ?: ZERO,
    lock = this?.lock.toInnerLock()
)

private fun AdSearchFilter?.toInnerSearchFilter() =
    InnerAdFilter(searchString = this?.searchString ?: "")

private fun AdCreateRequest.toInner() = InnerContext(
    command = CREATE,
    requestId = requestId?.let { InnerRequestId(it) } ?: InnerRequestId.NONE,
    adRequest = adCreate.toInnerAd(),
    workMode = debug.toInnerMode(),
    stubCase = debug.toInnerStub()
)

private fun AdReadRequest.toInner() = InnerContext(
    command = READ,
    requestId = requestId?.let { InnerRequestId(it) } ?: InnerRequestId.NONE,
    adRequest = adRead.toInner(),
    workMode = debug.toInnerMode(),
    stubCase = debug.toInnerStub()
)

private fun AdUpdateRequest.toInner() = InnerContext(
    command = UPDATE,
    requestId = requestId?.let { InnerRequestId(it) } ?: InnerRequestId.NONE,
    adRequest = adUpdate.toInnerAd(),
    workMode = debug.toInnerMode(),
    stubCase = debug.toInnerStub()
)

private fun AdDeleteRequest.toInner() = InnerContext(
    command = DELETE,
    requestId = requestId?.let { InnerRequestId(it) } ?: InnerRequestId.NONE,
    adRequest = adDelete.toInner(),
    workMode = debug.toInnerMode(),
    stubCase = debug.toInnerStub()
)

private fun AdSearchRequest.toInner() = InnerContext(
    command = SEARCH,
    requestId = requestId?.let { InnerRequestId(it) } ?: InnerRequestId.NONE,
    adFilterRequest = adFilter.toInnerSearchFilter(),
    workMode = debug.toInnerMode(),
    stubCase = debug.toInnerStub()
)
