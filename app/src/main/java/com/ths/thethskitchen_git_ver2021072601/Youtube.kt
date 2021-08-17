@file:Suppress("DEPRECATION")

package com.ths.thethskitchen_git_ver2021072601

import android.util.Log
import android.view.View
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.youtube.YouTube
import com.google.firebase.firestore.Query
import com.ths.thethskitchen_git_ver2021072601.databinding.ActivityListBinding
import kotlinx.coroutines.*
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.net.URLEncoder

suspend fun searchFireStore(searchString: String, adapter: RecyclerAdapter, listData: ArrayList<DList>, binding: ActivityListBinding) {
    val date = UtilFuncs().getKorDate()
    // 재료에서 검색
        val searchList = StringFuncs().makeSearch(searchString)
    App.db.collection("IName")//.orderBy("name")//.orderBy("id",Query.Direction.DESCENDING)
            .whereIn("name", searchList).limit(20).get()
        .addOnSuccessListener { result ->
            for (document in result) {
                val item = DList(document["id"] as String,"",0,0,"",0,"",0,
                    "",0,0,0,0,0,0,0,0,
                    0,0,"","",1)
                listData.add(item)
            }
        }.addOnFailureListener {
            Log.d("FireStore", "Fail")
        }.addOnCompleteListener {
            // 요리명에서 검색
                val searchList = StringFuncs().makeSearch(searchString)
            App.db.collection("DName")//.orderBy("name")//.orderBy("id",Query.Direction.DESCENDING)
                    .whereIn("name", searchList).limit(20).get()
                .addOnSuccessListener { result ->
                    for (document in result){
                        val item = DList(document["id"] as String,"",0,0,"",0,"",0,
                            "",0,0,0,0,0,0,0,0,
                            0,0,"","",1)
                        listData.add(item)
                        Log.d("FireStore","재료명 검색 ${item.id}")
                    }
                    // New 아이템
                    App.db.collection("DList")
                        .whereLessThanOrEqualTo("date", date)
                        .orderBy("date",Query.Direction.DESCENDING).limit(1).get()
                        .addOnSuccessListener { result ->
                            for (document in result){
                                val item = DList(document.id, "",0,0,"",0,"",0,
                                    "",0,0,0,0,0,0,0,0,
                                    0,0,document["vdeioID"] as String,"",4)
                                    listData.removeIf { it.id == item.id }
                                    listData.removeIf { it.video == item.video }
                                    listData.add(0,item)
                                    Log.d("FireStore","요리명 검색 ${item.id}")
                                }
                        }.addOnFailureListener {
                            Log.d("FireStore","New Fail")
                        }.addOnCompleteListener {
                            // Dlist에서 데이터 모두 검색
                            var cnt = 0 // 마지막 데이터 확인을 위한 카운터
                            for (i in 0 until listData.size) {
                                App.db.collection("DList").document(if (listData[i].id == ""){"0000"}else{listData[i].id})
                                    .get().addOnSuccessListener { document ->
                                        if (listData[i].id != "") {
                                            listData[i].date = document["date"] as Long
                                            listData[i].pretime = document["ptime"] as Long
                                            listData[i].preunit = document["punit"] as String
                                            listData[i].time = document["ctime"] as Long
                                            listData[i].timeunit = document["cunit"] as String
                                            listData[i].qunt = document["serv"] as Long
                                            listData[i].quntunit = document["sunit"] as String
                                            listData[i].start = 0
                                            listData[i].stove = document["stove"] as Long
                                            listData[i].oven = document["oven"] as Long
                                            listData[i].micro = document["micro"] as Long
                                            listData[i].blender = document["blender"] as Long
                                            listData[i].airfryer = document["air fryer"] as Long
                                            listData[i].multi = document["multi cooker"] as Long
                                            listData[i].steamer = document["steamer"] as Long
                                            listData[i].sousvide = document["sous vide"] as Long
                                            listData[i].grill = document["grill"] as Long
                                            listData[i].video = document["vdeioID"] as String
                                        }
                                    }.addOnFailureListener {
                                        Log.d("FireStore", "fail")
                                    }.addOnCompleteListener {
                                        cnt += 1
                                        if (cnt == listData.size) {
                                            listData.removeIf { it.video == "" }
                                            //정렬
                                            var tempList = listData.sortedByDescending { it.date }
                                                .sortedByDescending { it.id }
                                                .sortedByDescending { it.flag }
                                            tempList = tempList.distinctBy { it.video }
                                            listData.clear()
                                            listData.addAll(tempList)
                                            adapter.notifyDataSetChanged()
                                            CoroutineScope(Dispatchers.IO).launch {
                                                var cntFlag= 0 // flag 위한 카운터
                                                for (i in 0 until listData.size) {
                                                    listData[i] = SearchData().getYoutube(listData[i])// 유튜브 데이터
                                                    if (cntFlag < 3 && listData[i].flag == 1) {
                                                        listData[i].flag = 2
                                                        cntFlag += 1
                                                    }
                                                }
                                                withContext(Dispatchers.Main) {
                                                    for (item in listData){
//                                                        item.desc = ""
                                                        Log.d("FireStore",  "1 ${item.flag}")
                                                    }
                                                    listData.removeIf { it.name == "" }
                                                    adapter.notifyDataSetChanged()
                                                    binding.pbList.visibility = View.INVISIBLE
                                                }
                                            }

                                        }
                                    }
                            }

                        }

                    }.addOnFailureListener {
                        Log.d("FireStore","New Fail")
                    }
            }
}
fun searchYoutube(searchString: String): ArrayList<DList> {
    //Daum
    val search: String
    var dlist: ArrayList<DList>? = arrayListOf()

    try {
        search = URLEncoder.encode(searchString ,"UTF-8")
    }catch (e: UnsupportedEncodingException){
        return dlist!!
    }
    val langCode = UtilFuncs().getLanguage()
    var apiURL: String
    val requestHeader = HashMap<String, String>()
    requestHeader["Authorization"] = "KakaoAK c1cdf12b34171fae148670967c9a50e6"

    if  (langCode == "ko") {
        apiURL =
            "https://dapi.kakao.com/v2/search/vclip?sort=accuracy&size=10&query=$search+슬기로운식샤생활&cp=7b479cb3&p=1"
        dlist = SearchData().get(apiURL,requestHeader, dlist!!)
        apiURL =
            "https://dapi.kakao.com/v2/search/vclip?sort=accuracy&size=10&query=$search&cp=7b479cb3&p=1"
        dlist = SearchData().get(apiURL,requestHeader, dlist)
    }else{
        apiURL =
            "https://dapi.kakao.com/v2/search/vclip?sort=accuracy&size=20&query=$search&cp=7b479cb3&p=1"
        dlist = SearchData().get(apiURL,requestHeader, dlist!!)
    }
    val list = dlist.distinctBy { it.video }
    dlist.clear()
    dlist.addAll(list)
    Log.d("DaumAPI","Web search Size : ${dlist.size}")
    return dlist
}

class SearchData {
    fun get(apiURL: String, requestHeader: HashMap<String, String>, dlist: ArrayList<DList>): ArrayList<DList> {
        val con = connect(apiURL)
        val dlist = dlist
        try{
            if (con == null){
                throw  IOException("No con")
            }
            con.requestMethod = "GET"
            for (header: Map.Entry<String, String> in requestHeader.entries) {
                con.setRequestProperty(header.key,header.value)
            }

            val responseCode: Int = con.responseCode
            return if (responseCode == HttpURLConnection.HTTP_OK) {
                readBody(con.inputStream, dlist)
            }else{
                Log.d("DaumAPI","Fail")
                readBody(con.inputStream, dlist)
            }
        } catch (e: IOException){
            Log.d("DaumAPI","Get_IO")
            return dlist
        }finally {
            con?.disconnect()
        }
    }

    private fun readBody(body: InputStream?, dlist: ArrayList<DList>): ArrayList<DList> {
        try {
            body?.bufferedReader()?.use { it ->
                it.lines().forEach { line ->
                    val jsonObj = JSONObject(line)
                    val jsonArray = jsonObj.getJSONArray("documents")
                    for (i in 0 until jsonArray.length()) {
                        val list = jsonArray.getJSONObject(i)
                        var url = list.getString("url")
                        url = url.substring(url.length - 11,url.length)
                        val index = dlist.indexOfFirst { it.video == url }
                        if (index < 0){
                            var item = DList("","",0,0,"",0,"",0,
                                "",0,0,0,0,0,0,0,0,
                                0,0,url,"",0)
                            item = getFireStore(item)
                            dlist.add(item)
                        }
                    }
                }
            }
            return  dlist
        }catch (e: IOException){
            return dlist
        }catch (e: Exception) {
            return dlist
        }
    }

    private fun getFireStore(item: DList): DList {
App.db.collection("DList").whereEqualTo("vdeioID", item.video).limit(1)
            .get().addOnSuccessListener { result ->
                for (document in result ) {
                    item.id = document.id
                    item.pretime = document["ptime"] as Long
                    item.preunit = document["punit"] as String
                    item.time = document["ctime"] as Long
                    item.timeunit = document["cunit"] as String
                    item.qunt = document["serv"] as Long
                    item.quntunit = document["sunit"] as String
                    item.start = 0
                    item.stove = document["stove"] as Long
                    item.oven = document["oven"]    as Long
                    item.micro = document["micro"]  as Long
                    item.blender = document["blender"]  as Long
                    item.airfryer = document["air fryer"]   as Long
                    item.multi = document["multi cooker"]   as Long
                    item.steamer = document["steamer"]  as Long
                    item.sousvide = document["sous vide"]   as Long
                    item.grill = document["grill"]  as Long
                    item.video = document["vdeioID"] as String
                    if (item.flag == 0 && item.id != ""){
                        item.flag = 3
                    }
                }
            }
        return item
    }

   fun getYoutube(item: DList): DList {
        val item = item
        val apiKey = "AIzaSyA_Q3BlyHXBGxApWzSPsHKmhLN89-FO_T8"
        val langCode = UtilFuncs().getLanguage()
        val searchList = listOf("id", "snippet")
        val id = item.video
        val idList = listOf(id)

        try {
            val httpTransport = NetHttpTransport()
            val jsonFactory = JacksonFactory()
            val youtube = YouTube.Builder(httpTransport, jsonFactory) {
            }.setApplicationName("thethskitchen_git_ver2021072601").build()

            val video = youtube.videos().list(searchList)
            video.key = apiKey
            video.hl = langCode
            video.id = idList
            video.fields = "items(snippet/localized/title, snippet/localized/description)"
            val videoResponse = video.execute()
            val videoResultList = videoResponse.items
            val resultVideoList = videoResultList.iterator()

            while (resultVideoList.hasNext()) {
                val list = resultVideoList.iterator().next()
                item.name = list.snippet.localized.title
                item.desc = list.snippet.localized.description
            }
            return  item
        }catch (e: Exception){
            return item
        }
    }

    fun connect(apiURL: String): HttpURLConnection? {
        return try {
            val url = URL(apiURL)
            url.openConnection() as HttpURLConnection
        } catch (e: MalformedURLException) {
            Log.d("DaumAPI", "Connect_malformeURL")
            null
        } catch (e: IOException) {
            Log.d("DaumAPI", "Connect_IO")
            null
        }
    }


}
