package com.example.testapplication.MyApplication

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.example.testapplication.DI.App.AppComponent
import com.example.testapplication.DI.App.AppModule
import com.example.testapplication.DI.App.DaggerAppComponent

/**
 * Custom Application Object Holder
 */
class MyApplication : Application() {
    val myAppComponent : AppComponent by lazy {
        DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }

    override fun onCreate() {
        super.onCreate()
        context = this.applicationContext
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var context: Context? = null
        val appContext: Context?
            get() = context
    }

}