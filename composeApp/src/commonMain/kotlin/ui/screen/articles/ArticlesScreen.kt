package ui.screen.articles

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import articles.composeapp.generated.resources.Res
import articles.composeapp.generated.resources.app_name
import articles.composeapp.generated.resources.article_expired
import articles.composeapp.generated.resources.current_time
import articles.composeapp.generated.resources.empty_list
import navigation.Screen
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import ui.components.ArticlesTopAppBar
import ui.model.Article
import util.Constant
import util.extensions.toDateTimeString

@Composable
fun ArticlesScreen(navController: NavHostController) {

    val viewModel = koinViewModel<ArticlesViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    val sortType by viewModel.sortType.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiAction.collect { action ->
            when (action) {
                is ArticlesAction.ShowError -> {
                    snackbarHostState.showSnackbar(action.error)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            ArticlesTopAppBar(
                title = stringResource(Res.string.app_name),
                selectedSort = sortType,
                onSortClicked = {
                    viewModel.sortTypeSelected(selectedSortType = it)
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 8.dp),
                    text = "${stringResource(Res.string.current_time)} ${uiState.currentTimestamp.toDateTimeString()}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                PullToRefreshBox(
                    isRefreshing = uiState.isRefreshing,
                    onRefresh = {
                        viewModel.fetchArticles()
                    },
                    modifier = Modifier
                        .weight(1f)
                ) {
                    if (uiState.articles.isEmpty()) {
                        Text(
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(start = 16.dp, end = 16.dp)
                                .align(Alignment.Center),
                            text = stringResource(Res.string.empty_list),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Center
                        )
                    }

                    LazyColumn(Modifier.fillMaxSize()) {
                        itemsIndexed(uiState.articles) { index, item ->
                            ArticlePreview(item, uiState.currentTimestamp) { articleID ->
                                navController.currentBackStackEntry?.savedStateHandle?.set(
                                    Constant.ARTICLE_ID,
                                    articleID
                                )
                                navController.navigate(route = Screen.ArticleDetails.route)
                            }

                            if (index < uiState.articles.lastIndex) {
                                HorizontalDivider(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp),
                                    color = Color.Gray,
                                    thickness = 1.dp
                                )
                            }
                        }
                    }
                }
            }

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
            )
        }
    }


}

@Composable
fun ArticlePreview(
    article: Article,
    currentTimestamp: Long,
    onClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = {
                    onClick(article.id)
                },
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple()
            )
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp),
            text = article.title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            text = article.summary,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = Color.Black
        )

        Row {
            Text(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                text = article.timestamp.toDateTimeString(),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = if (article.timestamp > currentTimestamp) Color.Green else Color.Red
            )

            if (article.timestamp < currentTimestamp) {
                Text(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(start = 16.dp),
                    text = stringResource(Res.string.article_expired),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Red
                )
            }
        }
    }
}
