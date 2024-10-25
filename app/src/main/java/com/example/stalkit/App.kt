package com.example.stalkit

import android.app.Application
import com.example.stalkit.di.AppComponent
import com.example.stalkit.di.DaggerAppComponent

class App: Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }

}