package di

import domain.usecase.FetchArticlesUseCase
import domain.usecase.LoadArticleUseCase
import domain.usecase.ReadArticlesFromDatabaseUseCase
import neurohobbit.articles.database.di.platformDatabaseModule
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import ui.screen.articles.ArticlesViewModel
import ui.screen.details.ArticleDetailsViewModel

expect fun platformModule(): Module

fun initKoin(config: KoinAppDeclaration? = null) =
    startKoin {
        config?.invoke(this)
        modules(
            provideUseCaseModule, provideViewModelModule, platformDatabaseModule(),
            platformModule()
        )
    }

val provideUseCaseModule = module {
    singleOf(::FetchArticlesUseCase)
    singleOf(::ReadArticlesFromDatabaseUseCase)
    singleOf(::LoadArticleUseCase)
}

val provideViewModelModule = module {
    viewModel { ArticlesViewModel() }
    viewModel { ArticleDetailsViewModel() }
}