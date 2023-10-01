package ru.zyablov.otus.otuskotlin.adscar.mappers.v1

import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdCreateResponse
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdDeleteResponse
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdPermissions
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdReadResponse
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdResponseObject
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdSearchResponse
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdUpdateResponse
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.AdVisibility
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.Error
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.ResponseResult
import ru.zyablov.otus.otuskotlin.adscar.common.InnerContext
import ru.zyablov.otus.otuskotlin.adscar.common.models.InnerAd
import ru.zyablov.otus.otuskotlin.adscar.common.models.InnerAdPermissionClient
import ru.zyablov.otus.otuskotlin.adscar.common.models.InnerCommand
import ru.zyablov.otus.otuskotlin.adscar.common.models.InnerError
import ru.zyablov.otus.otuskotlin.adscar.common.models.InnerRequestId
import ru.zyablov.otus.otuskotlin.adscar.common.models.InnerState
import ru.zyablov.otus.otuskotlin.adscar.common.models.InnerVisibility
import ru.zyablov.otus.otuskotlin.adscar.mappers.v1.exceptions.UnknownInnerCommand

fun InnerContext.toTransport() = when (this.command) {
    InnerCommand.CREATE -> toTransportCreate()
    InnerCommand.READ -> toTransportRead()
    InnerCommand.UPDATE -> toTransportUpdate()
    InnerCommand.DELETE -> toTransportDelete()
    InnerCommand.SEARCH -> toTransportSearch()
    else -> throw UnknownInnerCommand(command)
}

private fun InnerContext.toTransportCreate() = AdCreateResponse(
    requestId = requestId.toTransportReqId(),
    result = if (state == InnerState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = this.errors.toTransportErrors(),
    ad = adResponse.toTransportAd()
)

private fun InnerContext.toTransportRead() = AdReadResponse(
    requestId = requestId.toTransportReqId(),
    result = if (state == InnerState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = this.errors.toTransportErrors(),
    ad = adResponse.toTransportAd()
)

private fun InnerContext.toTransportUpdate() = AdUpdateResponse(
    requestId = requestId.toTransportReqId(),
    result = if (state == InnerState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = this.errors.toTransportErrors(),
    ad = adResponse.toTransportAd()
)

private fun InnerContext.toTransportDelete() = AdDeleteResponse(
    requestId = requestId.toTransportReqId(),
    result = if (state == InnerState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = this.errors.toTransportErrors(),
    ad = adResponse.toTransportAd()
)

private fun InnerContext.toTransportSearch() = AdSearchResponse(
    requestId = requestId.toTransportReqId(),
    result = if (state == InnerState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = this.errors.toTransportErrors(),
    ads = adsResponse.toTransportAds()
)

private fun InnerRequestId.toTransportReqId() = asString().takeIf { it.isNotBlank() }

private fun InnerError.toTransportError() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() }
)

private fun InnerAdPermissionClient.toTransportPermission() = when (this) {
    InnerAdPermissionClient.READ -> AdPermissions.READ
    InnerAdPermissionClient.UPDATE -> AdPermissions.UPDATE
    InnerAdPermissionClient.MAKE_VISIBLE_OWNER -> AdPermissions.MAKE_VISIBLE_OWN
    InnerAdPermissionClient.MAKE_VISIBLE_GROUP -> AdPermissions.MAKE_VISIBLE_GROUP
    InnerAdPermissionClient.MAKE_VISIBLE_PUBLIC -> AdPermissions.MAKE_VISIBLE_PUBLIC
    InnerAdPermissionClient.DELETE -> AdPermissions.DELETE
}

private fun List<InnerError>.toTransportErrors() = this.map { it.toTransportError() }.takeIf { it.isNotEmpty() }

private fun Set<InnerAdPermissionClient>.toTransportPermissions() =
    this.map { it.toTransportPermission() }.toSet().takeIf { it.isNotEmpty() }

private fun InnerVisibility.toTransportVisibility() = when (this) {
    InnerVisibility.VISIBLE_PUBLIC -> AdVisibility.PUBLIC
    InnerVisibility.VISIBLE_TO_GROUP -> AdVisibility.REGISTERED_ONLY
    InnerVisibility.VISIBLE_TO_OWNER -> AdVisibility.OWNER_ONLY
    InnerVisibility.NONE -> null
}

private fun List<InnerAd>.toTransportAds() = this.map { it.toTransportAd() }.takeIf { it.isNotEmpty() }

private fun InnerAd.toTransportAd() = AdResponseObject(
    id = id.asString().takeIf { it.isNotBlank() },
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    ownerId = ownerId.asString().takeIf { it.isNotBlank() },
    visibility = visibility.toTransportVisibility(),
    permissions = permissionsClient.toTransportPermissions(),
    price = price
)
