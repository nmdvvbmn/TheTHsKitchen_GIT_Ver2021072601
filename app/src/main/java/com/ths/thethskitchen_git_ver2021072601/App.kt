package com.ths.thethskitchen_git_ver2021072601

import android.app.Application

class App: Application() {
    companion object {
        lateinit var prefs: THsSharedPreferences
    }

    override fun onCreate() {
        prefs = THsSharedPreferences(applicationContext)
        super.onCreate()
    }
}