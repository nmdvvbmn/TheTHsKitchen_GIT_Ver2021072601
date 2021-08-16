package com.ths.thethskitchen_git_ver2021072601

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.core.os.ConfigurationCompat
import java.io.FileNotFoundException
import java.net.URL
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

//이미지파일 로드 쓰레드
fun loadImage(imageUrl: String): Bitmap? {
    val url = URL(imageUrl)
    return try {
        val stream = url.openStream()
        BitmapFactory.decodeStream(stream)
    } catch (e: FileNotFoundException){
        null
    }
}

open class UtilFuncs {
    //언어 가져오기
    open fun getLanguage(): String {
        return App.prefs.getString("code", "en")
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
    open fun transUnit(unit: String): Int {
        return when (unit) {
            "분" -> R.string.min
            "시간" -> R.string.hour
            "일" -> R.string.day
            "주" -> R.string.week
            "개" -> R.string.pc
            "병" -> R.string.bottle
            "인분" -> R.string.serve
            "잔" -> R.string.glass
            else -> R.string.space
        }
    }

    // Float -> Int(String)
    open fun floatFormat(value: Float): String {
        val df = DecimalFormat("#.##")
        return df.format(value)
    }

    companion object {
        private const val OPTION_PHONE_LANGUAGE = "sys_def"

        /**
         * returns the locale to use depending on the preference value
         * when preference value = "sys_def" returns the locale of current system
         * else it returns the locale code e.g. "en", "bn" etc.
         */
        private fun getLocaleFromPrefCode(prefCode: String): Locale {
            val localeCode = if (prefCode != OPTION_PHONE_LANGUAGE) {
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

        private fun getLocalizedConfiguration(locale: Locale): Configuration {
            val config = Configuration()
            return config.apply {
                config.setLayoutDirection(locale)
                config.setLocale(locale)
            }
        }

        fun getLocalizedContext(baseContext: Context, prefLocaleCode: String): Context {
            val code: String = if (prefLocaleCode == "jw") {
                "jv"
            } else {
                prefLocaleCode
            }
            Log.d("Language", code)
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


        @Suppress("DEPRECATION")
        private fun getLocaleFromConfiguration(configuration: Configuration): Locale {
            return configuration.locales.get(0)
        }
    }
}

