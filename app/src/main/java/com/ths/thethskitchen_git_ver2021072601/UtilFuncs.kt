package com.ths.thethskitchen_git_ver2021072601

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.core.os.ConfigurationCompat
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import java.io.FileNotFoundException
import java.net.URL
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

//이미지파일 로드 쓰레드
suspend fun loadImage(imageUrl: String): Bitmap? {
    val url = URL(imageUrl)
    try {
        val stream = url.openStream()
        return  BitmapFactory.decodeStream(stream)
    } catch (e: FileNotFoundException){
        return  null
    }
}

open class UtilFuncs {
    //언어 가져오기
    open fun getLanguage(): String{
        return  App.prefs.getString("code","en")
    }

    // 한국시간 (기준시간)
    open fun getKorDate(): Int {
        val tz = TimeZone.getTimeZone("Asia/Seoul")
        val df = SimpleDateFormat("yyyyMMddHH")
        df.timeZone = tz
        val date = df.format(Date())
        return date.toInt()

    }

    //단위 변환(다국어 대응)
    open fun transUnit(unit: String) : Int {
        var tranUnit: Int = 0
        when (unit) {
            "분" -> tranUnit = R.string.min
            "시간" -> tranUnit = R.string.hour
            "일" -> tranUnit = R.string.day
            "주" -> tranUnit = R.string.week
            "개" -> tranUnit = R.string.pc
            "병" -> tranUnit = R.string.bottle
            "인분" -> tranUnit = R.string.serve
            "잔" -> tranUnit = R.string.glass
            else -> tranUnit = R.string.space
        }
        return  tranUnit
    }

    // Float -> Int(String)
    open fun floatFormat( value: Float): String {
        val df = DecimalFormat("#.##")
        return df.format(value)
    }

    companion object {
        const val OPTION_PHONE_LANGUAGE = "sys_def"

        /**
         * returns the locale to use depending on the preference value
         * when preference value = "sys_def" returns the locale of current system
         * else it returns the locale code e.g. "en", "bn" etc.
         */
        fun getLocaleFromPrefCode(prefCode: String): Locale{
            val localeCode = if(prefCode != OPTION_PHONE_LANGUAGE) {
                prefCode
            } else {
                ConfigurationCompat.getLocales(Resources.getSystem().configuration).get(0).language
            }
            return Locale(localeCode)
        }

        fun getLocalizedConfiguration(prefLocaleCode: String): Configuration {
            val locale = getLocaleFromPrefCode(prefLocaleCode)
            return getLocalizedConfiguration(locale)
        }

        fun getLocalizedConfiguration(locale: Locale): Configuration {
            val config = Configuration()
            return config.apply {
                config.setLayoutDirection(locale)
                config.setLocale(locale)
            }
        }

        fun getLocalizedContext(baseContext: Context, prefLocaleCode: String): Context {
            var code = ""
            if (prefLocaleCode == "jw"){
                code = "jv"
            }else{
                code = prefLocaleCode
            }
            Log.d("Language",code)
            val currentLocale = getLocaleFromPrefCode(code)
            val baseLocale = getLocaleFromConfiguration(baseContext.resources.configuration)
            Locale.setDefault(currentLocale)
            return if (!baseLocale.toString().equals(currentLocale.toString(), ignoreCase = true)) {
                val config = getLocalizedConfiguration(currentLocale)
                baseContext.createConfigurationContext(config)
                baseContext
            } else {
                baseContext
            }
        }

        fun applyLocalizedContext(baseContext: Context, prefLocaleCode: String) {
            val currentLocale = getLocaleFromPrefCode(prefLocaleCode)
            val baseLocale = getLocaleFromConfiguration(baseContext.resources.configuration)
            Locale.setDefault(currentLocale)
            if (!baseLocale.toString().equals(currentLocale.toString(), ignoreCase = true)) {
                val config = getLocalizedConfiguration(currentLocale)
                baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
            }
        }

        @Suppress("DEPRECATION")
        private fun getLocaleFromConfiguration(configuration: Configuration): Locale {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                configuration.locales.get(0)
            } else {
                configuration.locale
            }
        }

        fun getLocalizedResources(resources: Resources, prefLocaleCode: String): Resources {
            val locale = getLocaleFromPrefCode(prefLocaleCode)
            val config = resources.configuration
            @Suppress("DEPRECATION")
            config.locale = locale
            config.setLayoutDirection(locale)

            @Suppress("DEPRECATION")
            resources.updateConfiguration(config, resources.displayMetrics)
            return resources
        }
    }

}