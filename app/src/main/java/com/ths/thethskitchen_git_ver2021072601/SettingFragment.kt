
package com.ths.thethskitchen_git_ver2021072601

import android.os.Bundle
import android.util.Log
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference


class SettingFragment: PreferenceFragmentCompat() {
    override fun onCreatePreferences(p0: Bundle?, p1: String?) {
        preferenceManager.sharedPreferencesName = "Pref"    // preference명 설정
        addPreferencesFromResource(R.xml.setting_preference)

        val code = App.prefs.getString("code","en") // 언어설정
        val caption = App.prefs.getBoolean("caption", true) //자막 설정
        val play = App.prefs.getBoolean("play", true)   //자동재생 설정

        val list: ListPreference? = findPreference("code")
        list?.setSummary(list.entry)
        list?.setOnPreferenceChangeListener { preference, any ->
            val entry = context?.resources?.getStringArray(R.array.array_langCode)
            val index = entry?.indexOf(any.toString())
            val entryValue = context?.resources?.getStringArray(R.array.array_language)
            val value = entryValue?.get(index!!)
            list.summary = value.toString()
            Log.d("Preferenece", value.toString())
            true
        }
    }

}