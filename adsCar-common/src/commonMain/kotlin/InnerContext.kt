import kotlinx.datetime.Instant
import models.InnerAd
import models.InnerAdFilter
import models.InnerCommand
import models.InnerError
import models.InnerRequestId
import models.InnerState
import models.InnerWorkMode
import permissions.InnerPrincipalModel
import permissions.InnerUserPermissions
import repo.IAdRepository
import stubs.InnerStubs

data class InnerContext(
    var command: InnerCommand = InnerCommand.NONE,
    var state: InnerState = InnerState.NONE,
    var errors: MutableList<InnerError> = mutableListOf(),
    var settings: MkplCorSettings = MkplCorSettings.NONE,

    var workMode: InnerWorkMode = InnerWorkMode.PROD,
    var stubCase: InnerStubs = InnerStubs.NONE,

    var requestId: InnerRequestId = InnerRequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var adRequest: InnerAd = InnerAd(),
    var adFilterRequest: InnerAdFilter = InnerAdFilter(),
    var adResponse: InnerAd = InnerAd(),
    var adsResponse: MutableList<InnerAd> = mutableListOf(),

    var adValidating: InnerAd = InnerAd(),
    var adFilterValidating: InnerAdFilter = InnerAdFilter(),

    var adValidated: InnerAd = InnerAd(),
    var adFilterValidated: InnerAdFilter = InnerAdFilter(),

    var adRepo: IAdRepository = IAdRepository.NONE,
    var adRepoRead: InnerAd = InnerAd(), // То, что прочитали из репозитория
    var adRepoPrepare: InnerAd = InnerAd(), // То, что готовим для сохранения в БД
    var adRepoDone: InnerAd = InnerAd(), // Результат, полученный из БД
    var adsRepoDone: MutableList<InnerAd> = mutableListOf(), // Результат, полученный из БД

    var principal: InnerPrincipalModel = InnerPrincipalModel.NONE,
    val permissionsChain: MutableSet<InnerUserPermissions> = mutableSetOf(),
    var permitted: Boolean = false
)
