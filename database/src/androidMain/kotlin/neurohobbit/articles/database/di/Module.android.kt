package neurohobbit.articles.database.di

import android.content.Context
import neurohobbit.articles.database.AppDatabase
import neurohobbit.articles.database.db.ArticlesDatabase
import neurohobbit.articles.database.source.ArticlesDatabaseRepository
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.mp.KoinPlatform.getKoin

actual val platformContext: Any?
    get() = getKoin().get<Context>()

actual fun platformDatabaseModule(): Module = module {
    single<ArticlesDatabase> { AppDatabase.build(platformContext as Context) }
    single { ArticlesDatabaseRepository(get()) }
}
