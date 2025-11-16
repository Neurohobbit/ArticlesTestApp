package neurohobbit.articles.database.source

import kotlinx.coroutines.flow.Flow
import neurohobbit.articles.database.db.ArticlesDatabase
import neurohobbit.articles.database.entity.ArticleEntity

class ArticlesDatabaseRepository(private val articlesDatabase: ArticlesDatabase) {
    val articlesFlow: Flow<List<ArticleEntity>> = articlesDatabase.getArticleDao().getAll()

    suspend fun addArticle(articleEntity: ArticleEntity) {
        articlesDatabase.getArticleDao().insert(articleEntity)
    }

    suspend fun addArticlesList(articlesList: List<ArticleEntity>) {
        articlesDatabase.getArticleDao().insertAll(articlesList)
    }

    suspend fun clearArticles() {
        articlesDatabase.getArticleDao().clearAll()
    }

    fun getArticleById(id: String): Flow<ArticleEntity?> {
        return articlesDatabase.getArticleDao().getById(id)
    }
}
