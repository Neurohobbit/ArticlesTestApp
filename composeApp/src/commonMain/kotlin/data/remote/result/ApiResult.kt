package data.remote.result

sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(
        val errorCode: Int?,
        val errorTitle: String,
        val errorMessage: String
    ) : ApiResult<Nothing>()
}