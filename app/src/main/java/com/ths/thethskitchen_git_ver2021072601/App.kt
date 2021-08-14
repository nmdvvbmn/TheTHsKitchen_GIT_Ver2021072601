package com.ths.thethskitchen_git_ver2021072601

import android.app.Application
import android.content.Context

// Pref
class App: Application() {
    companion object {
        lateinit var prefs: THsSharedPreferences
    }

    override fun onCreate() {
        prefs = THsSharedPreferences(applicationContext)

        super.onCreate()
    }
//    private var currentLocaleContext: Context? = null
    override fun attachBaseContext(base: Context?) {
        prefs = THsSharedPreferences(base!!)
        super.attachBaseContext(UtilFuncs.getLocalizedContext(base, UtilFuncs().getLanguage()))
    }
}

//<boolean name="play" value="false" />
//<string name="code">ko</string>
//<boolean name="oven" value="true" />
//<boolean name="caption" value="false" />
//<boolean name="paly" value="false" />
//<boolean name="airfryer" value="false" />
//<boolean name="blender" value="false" />
//<boolean name="multi" value="false" />
//<boolean name="sous" value="false" />
//<boolean name="micro" value="false" />
//<boolean name="stove" value="true" />
//<boolean name="steam" value="false" />
//<boolean name="grill" value="false" />