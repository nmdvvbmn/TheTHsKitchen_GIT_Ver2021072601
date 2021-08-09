package com.ths.thethskitchen_git_ver2021072601

import android.util.Log
import com.google.api.client.http.HttpRequest
import com.google.api.client.http.HttpRequestInitializer
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.youtube.YouTube
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.net.URLEncoder

suspend fun searchYoutube(searchString: String): ArrayList<DList>? {
    //Daum
    var search: String? = null
    var dlist = arrayListOf<DList>()
    try {
        search = URLEncoder.encode(searchString ,"UTF-8")
    }catch (e: UnsupportedEncodingException){
        return null
    }
    val langCode = UtilFuncs().getLanguage()
    val apiURL: String
    if  (langCode == "ko") {
        apiURL = "https://dapi.kakao.com/v2/search/vclip?sort=accuracy&size=10&query=" + search + "+슬기로운식샤생활"
    }else{
        apiURL = "https://dapi.kakao.com/v2/search/vclip?sort=accuracy&size=10&query=" + search + "+youtube"
    }

    val requestHeader = HashMap<String, String>()
    requestHeader.put("Authorization", "KakaoAK c1cdf12b34171fae148670967c9a50e6")
    return get(apiURL,requestHeader)
}

fun get(apiURL: String, requestHeader: HashMap<String, String>): ArrayList<DList>? {
    val con = connect(apiURL)
    var dlist = arrayListOf<DList>()
    Log.d("DaumAPI","con : ${con}")
    try{
        if (con == null){
            throw  IOException("No con")
        }
        con?.requestMethod = "GET"
        for (header: Map.Entry<String, String> in requestHeader.entries) {
            con?.setRequestProperty(header.key,header.value)
        }

        var responseCode: Int = con!!.responseCode
        if (responseCode == HttpURLConnection.HTTP_OK) {
            return readBody(con.inputStream)
        }else{
            Log.d("DaumAPI","Fail")
            return readBody(con.inputStream)
        }
    } catch (e: IOException){
        Log.d("DaumAPI","Get_IO")
        return dlist
    }finally {
        con?.disconnect()
    }
}

fun readBody(body: InputStream?): ArrayList<DList>? {
    var dlist = arrayListOf<DList>()
    try {
        if (body != null) {
            body.bufferedReader().use {
                it.lines().forEach { line ->
                    val jsonObj = JSONObject(line)
                    val jsonArray = jsonObj.getJSONArray("documents")
                    for (i in 0..jsonArray.length() - 1) {
                        val list = jsonArray.getJSONObject(i)
                        var url = list.getString("url")
                        url = url.substring(url.length - 11,url.length)
                        var item: DList? = DList("","",0,0,"",0,"",0,
                            "",0,0,0,0,0,0,0,0,
                            0,0,url,"")
                        item = getYoutube(item!!)
                        if (item != null){
                            dlist.add(item)
                        }
                    }
                }
            }
        }
        return  dlist
    }catch (e: IOException){
            return null
    }catch (e: Exception) {
        return null
    }
}

fun getYoutube(item: DList): DList? {
    var item = item
    val api_key = "AIzaSyA_Q3BlyHXBGxApWzSPsHKmhLN89-FO_T8"
    val langCode = UtilFuncs().getLanguage()
    try {
        var httpTransport = NetHttpTransport()
//        val jsonFactory = JsonFactory()
        val jsonFactory = JacksonFactory()
        var youtube = YouTube.Builder(httpTransport, jsonFactory, HttpRequestInitializer {
            fun initialize(request: HttpRequest) {
            }
        }).setApplicationName("thethskitchen_git_ver2021072601").build()
        val searchList = listOf("id", "snippet")
        val id = listOf(item.video)
        var video = youtube.videos().list(searchList)
        video.setKey(api_key)
        video.setHl(langCode)
        video.setId(id)
        video.setFields("items(snippet/localized/title, snippet/localized/description)")
        var videoResponse = video.execute()
        var videoResultList = videoResponse.items
        var resultVideoList = videoResultList.iterator()

        while (resultVideoList.hasNext()) {
            val list = resultVideoList.iterator().next()
            item.name = list.snippet.localized.title
            item.desc = list.snippet.localized.description
        }
        return  item
    }catch (e: Exception){
        return null
    }
}

fun connect(apiURL: String): HttpURLConnection? {
    try {
        val url = URL(apiURL)
        return url.openConnection() as HttpURLConnection
    } catch (e: MalformedURLException) {
        Log.d("NaverAPI", "Connect_malformeURL")
        return null
    } catch (e: IOException) {
        Log.d("NaverAPI", "Connect_IO")
        return null
    }
}
