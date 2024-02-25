package com.example.task.app

import android.app.Application
import uk.co.accsoft.util.AzLog

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        AzLog.isBuildConfigDebug = true
    }
}