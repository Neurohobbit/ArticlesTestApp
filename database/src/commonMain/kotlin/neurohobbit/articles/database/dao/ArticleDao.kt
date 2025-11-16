package neurohobbit.articles.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import neurohobbit.articles.database.entity.ArticleEntity

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: ArticleEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articles: List<ArticleEntity>)

    @Query("SELECT * FROM articles WHERE id = :id LIMIT 1")
    fun getById(id: String): Flow<ArticleEntity?>

    @Query("SELECT * FROM articles ORDER BY expirationTimestamp DESC")
    fun getAll(): Flow<List<ArticleEntity>>

    @Query("DELETE FROM articles")
    suspend fun clearAll()
}