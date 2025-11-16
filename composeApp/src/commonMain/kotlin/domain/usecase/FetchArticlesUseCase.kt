package domain.usecase

import data.model.toArticleEntity
import data.remote.ApiRepository
import data.remote.result.ApiResult
import domain.model.ApiError
import domain.model.toApiError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import neurohobbit.articles.database.source.ArticlesDatabaseRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FetchArticlesUseCase : KoinComponent {

    private val apiRepository: ApiRepository by inject()
    private val databaseRepository: ArticlesDatabaseRepository by inject()

    suspend operator fun invoke(): Flow<ApiError?> {
        return apiRepository.fetchArticles()
            .onEach { result ->
                if (result is ApiResult.Success) {
                    databaseRepository.clearArticles()
                    databaseRepository.addArticlesList(
                        result.data.map {
                            it.toArticleEntity()
                        }
                    )
                }
            }
            .map { result ->
                when (result) {
                    is ApiResult.Error -> result.toApiError()
                    is ApiResult.Success -> null
                }
            }
            .flowOn(Dispatchers.IO)
    }
}