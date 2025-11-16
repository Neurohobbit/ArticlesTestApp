package neurohobbit.articles.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey val id: String,
    val title: String,
    val text: String,
    val summary: String,
    val expirationTimestamp: Long
)
