import repo.DbAdFilterRequest
import repo.DbAdIdRequest
import repo.DbAdRequest
import repo.DbAdResponse
import repo.DbAdsResponse
import repo.IAdRepository
import stubs.AdStub

class AdRepoStub() : IAdRepository {
    override suspend fun createAd(rq: DbAdRequest): DbAdResponse = DbAdResponse(
        data = AdStub.prepareResult { },
        isSuccess = true
    )

    override suspend fun readAd(rq: DbAdIdRequest): DbAdResponse = DbAdResponse(
        data = AdStub.prepareResult { },
        isSuccess = true
    )

    override suspend fun updateAd(rq: DbAdRequest): DbAdResponse = DbAdResponse(
        data = AdStub.prepareResult { },
        isSuccess = true
    )

    override suspend fun deleteAd(rq: DbAdIdRequest): DbAdResponse = DbAdResponse(
        data = AdStub.prepareResult { },
        isSuccess = true
    )

    override suspend fun searchAd(rq: DbAdFilterRequest): DbAdsResponse = DbAdsResponse(
        AdStub.prepareSearchList(filter = ""),
        isSuccess = true
    )
}
