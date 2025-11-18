# Articles Test Application

- **MVVM & Clean Architecture**
- **database** - database KMP shared module
- **SQLite & Room** - DB
- **MockWebServer** - mocking API requests
- **koin** - DI
- **Kotlin Coroutines** - multithreading
- **Jetpack Compose** - UI and Navigation
- **ktor** - API calls
- **Worker** - Regular updating of Articles

## Articles List Screen
Displays a list of articles from the database.  
If the list is empty, it displays a placeholder with information on how to update the list of articles.  
PullToRefreshBox is used for manual refresh. A Worker also automatically updates the list from the server once a day.

## Sorting
Articles can be sorted by: title, article freshness in ascending order, article freshness in descending order.

## Articles List Screen
It displays the details of the selected Article. All information is read from the database.   
The displayed data includes the title, summary, and the main text in HTML format. The [richeditor](https://github.com/MohamedRejeb/compose-rich-editor) framework is used to convert HTML into text.

## Error handling
Every third API request returns an error. The error is converted into a model (errorCode,
errorTitle, errorMessage).  
The error is displayed in the UI using a SnackbarHost.

## Offline mode
The application follows an offline-first model. All data is read from the database and refreshed from the server when necessary.

## MockWebServer
MockWebServer is used to mock server requests.  
**Every third request returns an error!**

## Worker
A Worker is used to update the list of articles from the server once a day.
Required conditions for running the Worker: Internet must be available, and the battery must not be low. Disabled conditions: the device does not need to be charging and does not need to be idle.

[![Demo Video](media/sample.gif)](media/sample.gif)
