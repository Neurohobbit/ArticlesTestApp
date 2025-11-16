package domain.model

import data.remote.result.ApiResult

data class ApiError(
    val errorCode: Int?,
    val errorTitle: String,
    val errorMessage: String
)

fun ApiResult.Error.toApiError() =
    ApiError(
        errorCode = errorCode,
        errorTitle = errorTitle,
        errorMessage = errorMessage
    )