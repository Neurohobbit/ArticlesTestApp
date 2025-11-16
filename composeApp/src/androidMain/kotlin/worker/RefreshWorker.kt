package worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import data.model.toArticleEntity
import data.remote.ApiRepository
import data.remote.result.ApiResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import neurohobbit.articles.database.source.ArticlesDatabaseRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.concurrent.TimeUnit

class RefreshWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams), KoinComponent {

    private val apiRepository: ApiRepository by inject()
    private val databaseRepository: ArticlesDatabaseRepository by inject()

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        apiRepository.fetchArticles().collect { result ->
            if (result is ApiResult.Success) {
                databaseRepository.clearArticles()
                databaseRepository.addArticlesList(
                    result.data.map {
                        it.toArticleEntity()
                    }
                )
            }
        }

        Result.success()
    }

    companion object {
        private const val WORK_NAME = "fetch_worker"
        fun enqueue(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .setRequiresCharging(false)
                .setRequiresDeviceIdle(false)
                .build()

            val workRequest = PeriodicWorkRequestBuilder<RefreshWorker>(1, TimeUnit.DAYS)
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
            )
        }
    }
}