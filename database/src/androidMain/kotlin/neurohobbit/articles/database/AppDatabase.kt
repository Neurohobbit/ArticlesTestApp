package neurohobbit.articles.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow
import neurohobbit.articles.database.dao.ArticleDao
import neurohobbit.articles.database.db.ArticlesDatabase
import neurohobbit.articles.database.entity.ArticleEntity

@Database(entities = [ArticleEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase(), ArticlesDatabase {
    abstract val articleDaoRoom: ArticleDao

    override fun getArticleDao(): ArticleDao = object : ArticleDao {
        override suspend fun insert(article: ArticleEntity) {
            articleDaoRoom.insert(article)
        }

        override suspend fun insertAll(articles: List<ArticleEntity>) {
            articleDaoRoom.insertAll(articles)
        }

        override fun getById(id: String): Flow<ArticleEntity?> {
            return articleDaoRoom.getById(id)
        }

        override fun getAll(): Flow<List<ArticleEntity>> {
            return articleDaoRoom.getAll()
        }

        override suspend fun clearAll() {
            articleDaoRoom.clearAll()
        }

    }

    companion object {
        fun build(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "articles.db"
            ).build()
        }
    }
}


