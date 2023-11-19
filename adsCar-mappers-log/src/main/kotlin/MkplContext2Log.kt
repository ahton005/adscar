import kotlinx.datetime.Clock
import models.InnerAd
import models.InnerAdFilter
import models.InnerAdId
import models.InnerCommand
import models.InnerError
import models.InnerRequestId
import models.InnerUserId
import models.InnerVisibility
import ru.zyablov.otus.otuskotlin.adscar.log.models.AdFilterLog
import ru.zyablov.otus.otuskotlin.adscar.log.models.AdLog
import ru.zyablov.otus.otuskotlin.adscar.log.models.CommonLogModel
import ru.zyablov.otus.otuskotlin.adscar.log.models.ErrorLogModel
import ru.zyablov.otus.otuskotlin.adscar.log.models.LogModel

fun InnerContext.toLog(logId: String) = CommonLogModel(
    messageTime = Clock.System.now().toString(),
    logId = logId,
    source = "ok-marketplace",
    ad = toMkplLog(),
    errors = errors.map { it.toLog() }
)

fun InnerContext.toMkplLog(): LogModel? {
    val adNone = InnerAd()
    return LogModel(
        requestId = requestId.takeIf { it != InnerRequestId.NONE }?.asString(),
        operation = command.toLogModel(),
        requestAd = adRequest.takeIf { it != adNone }?.toLog(),
        responseAd = adResponse.takeIf { it != adNone }?.toLog(),
        responseAds = adsResponse.takeIf { it.isNotEmpty() }?.filter { it != adNone }?.map { it.toLog() },
        requestFilter = adFilterRequest.takeIf { it != InnerAdFilter() }?.toLog()
    ).takeIf { it != LogModel() }
}

private fun InnerCommand.toLogModel(): LogModel.Operation? = when (this) {
    InnerCommand.CREATE -> LogModel.Operation.CREATE
    InnerCommand.READ -> LogModel.Operation.READ
    InnerCommand.UPDATE -> LogModel.Operation.UPDATE
    InnerCommand.DELETE -> LogModel.Operation.DELETE
    InnerCommand.SEARCH -> LogModel.Operation.SEARCH
    InnerCommand.INIT -> LogModel.Operation.INIT
    InnerCommand.FINISH -> LogModel.Operation.FINISH
    InnerCommand.NONE -> null
}

private fun InnerAdFilter.toLog() = AdFilterLog(
    searchString = searchString.takeIf { it.isNotBlank() },
    ownerId = ownerId.takeIf { it != InnerUserId.NONE }?.asString()
)

fun InnerError.toLog() = ErrorLogModel(
    message = message.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    code = code.takeIf { it.isNotBlank() },
    level = level.name
)

fun InnerAd.toLog() = AdLog(
    id = id.takeIf { it != InnerAdId.NONE }?.asString(),
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    visibility = visibility.takeIf { it != InnerVisibility.NONE }?.name,
    ownerId = ownerId.takeIf { it != InnerUserId.NONE }?.asString(),
    permissions = permissionsClient.takeIf { it.isNotEmpty() }?.map { it.name }?.toSet()
)
