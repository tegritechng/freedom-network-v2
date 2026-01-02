package com.threelineng.freedomnetwork.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App(): Application() {

    override fun onCreate() {
        super.onCreate()
//        initializeDevice(this)
//        BaseUtils.init(this)

    }

}