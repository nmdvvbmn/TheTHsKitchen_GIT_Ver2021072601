package com.ths.thethskitchen_git_ver2021072601

import android.content.Context
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager


class SettingFragment: PreferenceFragmentCompat() {
    override fun onCreatePreferences(p0: Bundle?, p1: String?) {
        preferenceManager.sharedPreferencesName = "Pref"
        addPreferencesFromResource(R.xml.setting_preference)

        val code = App.prefs.getString("code","en")
        val caption = App.prefs.getBoolean("caption", true)
        val play = App.prefs.getBoolean("play", true)
//        var code = App.prefs.getString("code","en")
//        var caption = App.prefs.getBoolean("caption", true)
//        var play = App.prefs.getBoolean("play", true)



    }
}