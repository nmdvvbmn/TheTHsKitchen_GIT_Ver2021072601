package com.ths.thethskitchen_git_ver2021072601

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestoreSettings
import java.util.*

// Pref
class App: Application() {
    companion object {
        lateinit var prefs: THsSharedPreferences
        var mInterstitialAd: InterstitialAd? = null
        var changed = false
        var ad = true
        var init = true
        const val dbVer = 1
        const val dbName = "THsKitchen.db"
        @SuppressLint("StaticFieldLeak")
        lateinit var db: FirebaseFirestore
        val settings = firestoreSettings {
            isPersistenceEnabled = true
            cacheSizeBytes = FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED
        }
    }

    override fun onCreate() {
        prefs = THsSharedPreferences(applicationContext)
        db = FirebaseFirestore.getInstance()
        db.firestoreSettings = settings
        
        val adRequestF = AdRequest.Builder().build()
        InterstitialAd.load(this,"ca-app-pub-3974703588591994/3980667216", adRequestF, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                mInterstitialAd = null
                Log.d("ADMOB","Fullscreen Load Fail")
                Log.d("ADMOB","${adError}")
            }
            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd
                Log.d("ADMOB","Fullscreen onLoad ")
            }
        })

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