package ui.screen.articles

import ui.model.Article

data class ArticlesState(
    val articles: List<Article> = emptyList(),
    val isRefreshing: Boolean = false,
    val currentTimestamp: Long = 0
)

sealed class ArticlesAction {
    data class ShowError(val error: String) : ArticlesAction()
}

sealed class SortType() {
    object ByTitle : SortType()
    object ByDateAsc : SortType()
    object ByDateDesc : SortType()
}
