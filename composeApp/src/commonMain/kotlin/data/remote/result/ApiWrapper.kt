package data.remote.result

import io.ktor.client.HttpClient
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ApiWrapper(private val client: HttpClient) {

    suspend fun <T> request(
        block: suspend HttpClient.() -> HttpResponse,
        transform: suspend (HttpResponse) -> T
    ): Flow<ApiResult<T>> = flow {
        try {
            val response = client.block()

            if (response.status.isSuccess()) {
                val body = transform(response)
                emit(ApiResult.Success(body))

            } else {
                emit(
                    ApiResult.Error(
                        errorCode = response.status.value,
                        errorTitle = "HTTP ${response.status.value}",
                        errorMessage = response.bodyAsText()
                    )
                )
            }

        } catch (t: Throwable) {
            val errorCode = when (t) {
                is io.ktor.client.plugins.ClientRequestException ->
                    t.response.status.value

                is io.ktor.client.plugins.ServerResponseException ->
                    t.response.status.value

                else -> null
            }

            emit(
                ApiResult.Error(
                    errorCode = errorCode,
                    errorTitle = t::class.simpleName ?: "Unknown error",
                    errorMessage = t.message ?: "No message"
                )
            )
        }
    }
}