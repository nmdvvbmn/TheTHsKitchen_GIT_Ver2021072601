package com.ths.thethskitchen_git_ver2021072601

import android.content.Context
import android.content.SharedPreferences

class THsSharedPreferences(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("Pref", Context.MODE_PRIVATE)

    fun getString(key: String, defValues: String): String {
        return prefs.getString(key,defValues).toString()
    }

    fun setString(key: String, defValues: String) {
        prefs.edit().putString(key,defValues).apply()
    }

    fun getBoolean(key: String, defValues: Boolean): Boolean {
        return prefs.getBoolean(key,defValues)
    }

    fun setBoolena(key: String, defValues: Boolean) {
        prefs.edit().putBoolean(key,defValues).apply()
    }
}