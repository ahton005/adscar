package repo

import models.InnerAd
import models.InnerError

data class DbAdResponse(
    override val data: InnerAd?,
    override val isSuccess: Boolean,
    override val errors: List<InnerError> = listOf()
) : IDbResponse<InnerAd> {

    companion object {
        val MOCK_SUCCESS_EMPTY = DbAdResponse(null, true)
        fun success(result: InnerAd) = DbAdResponse(result, true)
        fun error(errors: List<InnerError>) = DbAdResponse(null, false, errors)
        fun error(error: InnerError) = DbAdResponse(null, false, listOf(error))
    }
}
