package com.ths.thethskitchen_git_ver2021072601

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
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

        Log.d("dateTest", date)
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


}