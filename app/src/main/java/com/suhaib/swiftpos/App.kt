package com.suhaib.swiftpos

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        Timber.plant(Timber.DebugTree())
    }

    companion object {
        @get:Synchronized
        lateinit var instance: App
            private set

        val context: Context
            get() = instance.applicationContext
    }
}