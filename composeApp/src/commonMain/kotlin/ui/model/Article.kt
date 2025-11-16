package ui.model

import neurohobbit.articles.database.entity.ArticleEntity

data class Article(
    val id: String,
    val title: String,
    val text: String,
    val summary: String,
    val timestamp: Long
)

fun ArticleEntity.toArticle() = Article(
    id = id,
    title = title,
    text = text,
    summary = summary,
    timestamp = expirationTimestamp
)
