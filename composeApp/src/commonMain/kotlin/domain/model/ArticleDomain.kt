package domain.model

import neurohobbit.articles.database.entity.ArticleEntity

data class ArticleDomain(
    val id: String,
    val title: String,
    val text: String,
    val timestamp: Long
)