
package com.ths.thethskitchen_git_ver2021072601

import android.content.Intent
import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat

class SettingFragment: PreferenceFragmentCompat() {
    override fun onCreatePreferences(p0: Bundle?, p1: String?) {
        preferenceManager.sharedPreferencesName = "Pref"    // preference명 설정
        addPreferencesFromResource(R.xml.setting_preference)

        App.prefs.getString("code","en") // 언어설정
        App.prefs.getBoolean("caption", true) //자막 설정
        App.prefs.getBoolean("play", true)   //자동재생 설정

        val list: ListPreference? = findPreference("code")
        list?.summary = list?.entry
        list?.setOnPreferenceChangeListener { _, any ->
            val entry = context?.resources?.getStringArray(R.array.array_langCode)
            val index = entry?.indexOf(any.toString())
            val entryValue = context?.resources?.getStringArray(R.array.array_language)
            val value = entryValue?.get(index!!)
            list.summary = value.toString()
//            UtilFuncs().setLocale(requireContext(),UtilFuncs().getLanguage())
            val intent = Intent(context, SettingActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            startActivity(intent)
            true
        }
    }

}