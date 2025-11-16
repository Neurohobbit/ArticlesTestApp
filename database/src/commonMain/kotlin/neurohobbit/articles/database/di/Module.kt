package neurohobbit.articles.database.di

import org.koin.core.module.Module

expect val platformContext: Any?

expect fun platformDatabaseModule(): Module