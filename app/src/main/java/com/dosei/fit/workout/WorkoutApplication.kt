package com.dosei.fit.workout

import android.app.Application
import com.dosei.fit.workout.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class WorkoutApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@WorkoutApplication)
            modules(appModule)
        }
    }
}