package data.remote

import data.model.ArticleApiModel
import data.remote.result.ApiResult
import data.remote.result.ApiWrapper
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow

actual class ApiRepository(private val api: ApiWrapper) {
    actual suspend fun fetchArticles(): Flow<ApiResult<List<ArticleApiModel>>> {
        delay(2000L)
        return api.request(
            block = {
                get("/articles")
            },
            transform = { response ->
                response.body<List<ArticleApiModel>>()
            }
        )
    }
}
