import kotlinx.datetime.Instant
import models.InnerAd
import models.InnerAdFilter
import models.InnerCommand
import models.InnerError
import models.InnerRequestId
import models.InnerState
import models.InnerWorkMode
import stubs.InnerStubs

data class InnerContext(
    val command: InnerCommand = InnerCommand.NONE,
    val state: InnerState = InnerState.NONE,
    val errors: List<InnerError> = listOf(),

    val workMode: InnerWorkMode = InnerWorkMode.PROD,
    val stubCase: InnerStubs = InnerStubs.NONE,

    val requestId: InnerRequestId = InnerRequestId.NONE,
    val timeStart: Instant = Instant.NONE,
    val adRequest: InnerAd = InnerAd(),
    val adFilterRequest: InnerAdFilter = InnerAdFilter(),
    val adResponse: InnerAd = InnerAd(),
    val adsResponse: List<InnerAd> = listOf()
)
