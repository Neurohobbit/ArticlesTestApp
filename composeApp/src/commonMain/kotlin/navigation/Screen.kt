package navigation

sealed class Screen(val route: String) {
    object Articles : Screen("articles")
    object ArticleDetails : Screen("article_details")
}