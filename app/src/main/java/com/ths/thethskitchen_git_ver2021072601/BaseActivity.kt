package com.ths.thethskitchen_git_ver2021072601

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity: AppCompatActivity() { //You can use your preferred activity instead of AppCompatActivity
    private lateinit var oldPrefLocaleCode : String
    /**
     * updates the toolbar text locale if it set from the android:label property of Manifest
     */
    private fun resetTitle() {
        try {
            val label = packageManager.getActivityInfo(componentName, PackageManager.GET_META_DATA).labelRes;
            if (label != 0) {
                setTitle(label);
            }
        } catch (e: PackageManager.NameNotFoundException) {}
    }

    override fun attachBaseContext(newBase: Context) {
        oldPrefLocaleCode = UtilFuncs().getLanguage()
        if(oldPrefLocaleCode == "jw"){
            oldPrefLocaleCode = "jv"
        }
        applyOverrideConfiguration(UtilFuncs.getLocalizedConfiguration(oldPrefLocaleCode))
        super.attachBaseContext(newBase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resetTitle()
    }

    override fun onResume() {
        var currentLocaleCode = UtilFuncs().getLanguage()
        if (currentLocaleCode == "jw"){
            currentLocaleCode = "jv"
        }
        if(oldPrefLocaleCode != currentLocaleCode){
            recreate() //locale is changed, restart the activty to update
            oldPrefLocaleCode = currentLocaleCode
        }
        super.onResume()
    }
}