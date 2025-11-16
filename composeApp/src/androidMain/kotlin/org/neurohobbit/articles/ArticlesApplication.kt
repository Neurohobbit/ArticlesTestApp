package org.neurohobbit.articles

import android.app.Application
import worker.RefreshWorker
import di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent

class ArticlesApplication : Application(), KoinComponent {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidLogger()
            androidContext(this@ArticlesApplication)
        }
        launchBackgroundSync()
    }

    private fun launchBackgroundSync() {
        RefreshWorker.enqueue(this)
    }
}