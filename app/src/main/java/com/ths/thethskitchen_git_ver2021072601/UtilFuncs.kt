package com.ths.thethskitchen_git_ver2021072601

import android.content.Context
import android.util.Log
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

open class UtilFuncs {

    open fun getLanguage(): String{
        return  App.prefs.getString("code","en")
    }

    open fun getKorDate(): Int {
        val tz = TimeZone.getTimeZone("Asia/Seoul")
        val df = SimpleDateFormat("yyyyMMddHH")
        df.timeZone = tz
        val date = df.format(Date())

        Log.d("dateTest", date)
        return date.toInt()

    }

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

    open fun floatFormat( value: Float): String {
        val df = DecimalFormat("#.##")
        return df.format(value)
    }


}