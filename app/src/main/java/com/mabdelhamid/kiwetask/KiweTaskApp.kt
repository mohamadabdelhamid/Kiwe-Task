package com.mabdelhamid.kiwetask

import android.app.Application
import com.mabdelhamid.kiwetask.data.network.NetworkManager

class KiweTaskApp : Application() {
    override fun onCreate() {
        super.onCreate()
        NetworkManager.init()
    }
}