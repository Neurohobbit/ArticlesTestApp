package data.model

import kotlinx.serialization.Serializable
import neurohobbit.articles.database.entity.ArticleEntity
import kotlin.time.ExperimentalTime

@Serializable
data class ArticleApiModel(
    val id: String = "",
    val title: String,
    val text: String,
    val summary: String,
    val expirationTimestamp: Long
)

@OptIn(ExperimentalTime::class)
fun ArticleApiModel.toArticleEntity() = ArticleEntity(
    id = id,
    title = title,
    text = text,
    summary = summary,
    expirationTimestamp = expirationTimestamp
)
