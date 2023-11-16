package repo

import models.InnerError

interface IDbResponse<T> {
    val data: T?
    val isSuccess: Boolean
    val errors: List<InnerError>
}
