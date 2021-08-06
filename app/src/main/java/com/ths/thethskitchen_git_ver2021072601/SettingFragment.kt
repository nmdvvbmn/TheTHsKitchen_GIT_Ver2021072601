package com.ths.thethskitchen_git_ver2021072601

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat


class SettingFragment: PreferenceFragmentCompat() {
    override fun onCreatePreferences(p0: Bundle?, p1: String?) {
        preferenceManager.sharedPreferencesName = "Pref"    // preference명 설정
        addPreferencesFromResource(R.xml.setting_preference)

        val code = App.prefs.getString("code","en") // 언어설정
        val caption = App.prefs.getBoolean("caption", true) //자막 설정
        val play = App.prefs.getBoolean("play", true)   //자동재생 설정
        
    }
}