package domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import neurohobbit.articles.database.source.ArticlesDatabaseRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ui.model.Article
import ui.model.toArticle

class LoadArticleUseCase : KoinComponent {

    private val databaseRepository: ArticlesDatabaseRepository by inject()

    operator fun invoke(articleId: String): Flow<Article?> {
        return databaseRepository.getArticleById(articleId)
            .map { it?.toArticle() }
    }
}