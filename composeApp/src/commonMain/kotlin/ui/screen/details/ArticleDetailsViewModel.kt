package ui.screen.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.usecase.LoadArticleUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ArticleDetailsViewModel() : ViewModel(), KoinComponent {

    private val loadArticleUseCase: LoadArticleUseCase by inject()

    private val _uiState = MutableStateFlow(ArticleDetailsState())
    val uiState: StateFlow<ArticleDetailsState> get() = _uiState.asStateFlow()

    fun loadArticle(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            loadArticleUseCase(id).collect {
                it?.let {
                    _uiState.emit(
                        uiState.value.copy(
                            article = it
                        )
                    )
                }
            }
        }
    }
}