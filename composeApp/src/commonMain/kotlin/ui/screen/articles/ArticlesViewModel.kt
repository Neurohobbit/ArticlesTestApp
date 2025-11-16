package ui.screen.articles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.usecase.FetchArticlesUseCase
import domain.usecase.ReadArticlesFromDatabaseUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ui.model.Article
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class ArticlesViewModel() : ViewModel(), KoinComponent {

    private val fetchArticlesUseCase: FetchArticlesUseCase by inject()
    private val readArticlesFromDatabaseUseCase: ReadArticlesFromDatabaseUseCase by inject()

    private val _uiState = MutableStateFlow(ArticlesState())
    val uiState: StateFlow<ArticlesState> get() = _uiState.asStateFlow()

    private val _sortType = MutableStateFlow<SortType>(SortType.ByTitle)
    val sortType: StateFlow<SortType> get() = _sortType.asStateFlow()

    private val _uiAction = MutableSharedFlow<ArticlesAction>()
    val uiAction: SharedFlow<ArticlesAction> get() = _uiAction.asSharedFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            readArticlesFromDatabaseUseCase()
                .combine(_sortType) { articles, sortType ->
                    sortArticles(articles, sortType)
                }
                .collect { sortedArticles ->
                    _uiState.emit(
                        uiState.value.copy(articles = sortedArticles)
                    )
                }
        }

        viewModelScope.launch(Dispatchers.IO) {
            readArticlesFromDatabaseUseCase().collect { articles ->
                _uiState.emit(
                    uiState.value.copy(
                        articles = articles
                    )
                )
            }
        }

        viewModelScope.launch(Dispatchers.Default) {
            while (isActive) {
                _uiState.emit(
                    uiState.value.copy(
                        currentTimestamp = Clock.System.now().toEpochMilliseconds()
                    )
                )
                delay(1000)
            }
        }
    }

    fun fetchArticles() {
        viewModelScope.launch(Dispatchers.IO) {
            setIsRefreshing(true)

            fetchArticlesUseCase().collect { error ->
                error?.let {
                    _uiAction.emit(
                        ArticlesAction.ShowError("${it.errorTitle}. ${it.errorMessage}")
                    )
                }
                setIsRefreshing(false)
            }
        }
    }

    fun sortTypeSelected(selectedSortType: SortType) {
        viewModelScope.launch(Dispatchers.Default) {
            _sortType.emit(selectedSortType)
        }
    }

    private suspend fun setIsRefreshing(isRefreshing: Boolean) {
        _uiState.emit(
            uiState.value.copy(
                isRefreshing = isRefreshing
            )
        )
    }

    private fun sortArticles(articles: List<Article>, sortType: SortType): List<Article> {
        return when (sortType) {
            SortType.ByDateAsc -> articles.sortedByDescending { it.timestamp }
            SortType.ByDateDesc -> articles.sortedBy { it.timestamp }
            SortType.ByTitle -> articles.sortedBy { it.title.lowercase() }
        }
    }

}
