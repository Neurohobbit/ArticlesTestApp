package neurohobbit.articles.database.db

import neurohobbit.articles.database.dao.ArticleDao

interface ArticlesDatabase {
    fun getArticleDao(): ArticleDao
}