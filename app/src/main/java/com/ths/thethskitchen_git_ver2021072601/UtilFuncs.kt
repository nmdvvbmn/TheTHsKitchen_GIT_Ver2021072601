package com.ths.thethskitchen_git_ver2021072601

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

open class UtilFuncs {

    open fun getLanguage(): String{
        var language = Locale.getDefault().getLanguage()
        if (language != null) {
            val codeList: List<String> = listOf("ko", "en", "zh-CN", "zh-TW", "es", "ar", "hi",
                "bn", "pt",	"ru", "ja",	"jw", "tr", "fr", "de", "te", "mr", "ur", "vi", "ta", "it",
                "fa", "ms", "gu")
            if (codeList.any { it == language } ) {
                return language
            }else{
                return "en"
            }
        }else{
            return  "en"
        }
    }

    open fun getKorDate(): Int {
        val tz = TimeZone.getTimeZone("Asia/Seoul")
        val df = SimpleDateFormat("yyyyMMddHH")
        df.timeZone = tz
        val date = df.format(Date())

        Log.d("dateTest", date)
        return date.toInt()

    }

}