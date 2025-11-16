package data.remote

import data.model.ArticleApiModel
import data.remote.result.ApiResult
import kotlinx.coroutines.flow.Flow

expect class ApiRepository {
    suspend fun fetchArticles(): Flow<ApiResult<List<ArticleApiModel>>>
}