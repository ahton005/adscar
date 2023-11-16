package repo

import models.InnerAd
import models.InnerError

data class DbAdsResponse(
    override val data: List<InnerAd>?,
    override val isSuccess: Boolean,
    override val errors: List<InnerError> = listOf()
) : IDbResponse<List<InnerAd>> {

    companion object {
        val MOCK_SUCCESS_EMPTY = DbAdsResponse(emptyList(), true)
        fun success(result: List<InnerAd>) = DbAdsResponse(result, true)
        fun error(errors: List<InnerError>) = DbAdsResponse(null, false, errors)
        fun error(error: InnerError) = DbAdsResponse(null, false, listOf(error))
    }
}
