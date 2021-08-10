package com.ths.thethskitchen_git_ver2021072601

import android.util.Log
import android.view.View
import com.google.api.client.http.HttpRequest
import com.google.api.client.http.HttpRequestInitializer
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.youtube.YouTube
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.ths.thethskitchen_git_ver2021072601.databinding.ActivityListBinding
import kotlinx.coroutines.*
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.net.URLEncoder

suspend fun getYoutube(item: DList): DList {
    val api_key = "AIzaSyA_Q3BlyHXBGxApWzSPsHKmhLN89-FO_T8"
    val langCode = UtilFuncs().getLanguage()
    val searchList = listOf("id", "snippet")
    val id = item.video
    val idList = listOf(id)

    try {
        val httpTransport = NetHttpTransport()
//        val jsonFactory = JsonFactory()
        val jsonFactory = JacksonFactory()
        val youtube = YouTube.Builder(httpTransport, jsonFactory, HttpRequestInitializer {
            fun initialize(request: HttpRequest) {
            }
        }).setApplicationName("thethskitchen_git_ver2021072601").build()

        val video = youtube.videos().list(searchList)
        video.setKey(api_key)
        video.setHl(langCode)
        video.setId(idList)
        video.setFields("items(snippet/localized/title, snippet/localized/description)")
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
suspend fun searchFireStore(searchString: String, adapter: RecyclerAdapter, listData: ArrayList<DList>, binding: ActivityListBinding) {
    var dlist = arrayListOf<DList>()
    val db = FirebaseFirestore.getInstance()
    val date = UtilFuncs().getKorDate()
    db.collection("IName").whereGreaterThanOrEqualTo("name", searchString)
        .whereLessThanOrEqualTo("name",searchString + "\uf8ff").limit(7).get()
        .addOnSuccessListener { result ->
            for (document in result) {
                val item = DList(document["id"] as String,"",0,0,"",0,"",0,
                    "",0,0,0,0,0,0,0,0,
                    0,0,"","",0)
                dlist.add(item)
            }
        }.addOnCompleteListener {
            val distinctList = dlist.distinctBy { it.id}.sortedByDescending { it.id }
            dlist.clear()
            dlist.addAll(distinctList)
            db.collection("DName").whereGreaterThanOrEqualTo("name", searchString)
                .whereLessThanOrEqualTo("name",searchString + "\uf8ff").limit(7).get()
                .addOnSuccessListener { result ->
                    for (document in result){
                        val item = DList(document["id"] as String,"",0,0,"",0,"",0,
                            "",0,0,0,0,0,0,0,0,
                            0,0,"","",0)
                        dlist.add(item)
                    }
                }
                .addOnCompleteListener {
                    val distinctList = dlist.distinctBy { it.id}.sortedByDescending { it.id }
                    dlist.clear()
                    dlist.addAll(distinctList)
                    var cnt = 0
                    for (index in 0..dlist.size - 1  ){
                        val id = dlist[index].id
                        Log.d("FireStore",id)
                        db.collection("DList").document(id)
                            .get().addOnSuccessListener { document ->
                                dlist[index].date = document["date"] as Long
                                dlist[index].pretime = document["ptime"] as Long
                                dlist[index].preunit = document["punit"] as String
                                dlist[index].time = document["ctime"] as Long
                                dlist[index].timeunit = document["cunit"] as String
                                dlist[index].qunt = document["serv"] as Long
                                dlist[index].quntunit = document["sunit"] as String
                                dlist[index].start = 0
                                dlist[index].stove = document["stove"] as Long
                                dlist[index].oven = document["oven"]    as Long
                                dlist[index].micro = document["micro"]  as Long
                                dlist[index].blender = document["blender"]  as Long
                                dlist[index].airfryer = document["air fryer"]   as Long
                                dlist[index].multi = document["multi cooker"]   as Long
                                dlist[index].steamer = document["steamer"]  as Long
                                dlist[index].sousvide = document["sous vide"]   as Long
                                dlist[index].grill = document["grill"]  as Long
                                dlist[index].video = document["vdeioID"] as String
                                if (dlist[index].date <= date){
                                    if ( cnt < 3) {
                                        dlist[index].flag = 2
                                    }else{
                                        dlist[index].flag = 0
                                    }
                                    cnt = cnt + 1
                                    listData.add(0,dlist[index])
                                }
                            }.addOnFailureListener {
                                Log.d("FireStore","Fail")
                            }.addOnCompleteListener {
                                if (index == dlist.size - 1){
                                    CoroutineScope(Dispatchers.IO).launch {
                                        var tempList = listData.distinctBy { it.video }
                                        tempList = tempList.distinctBy { it.id }
                                        listData.clear()
                                        listData.addAll(tempList)
                                        for (i in 0..listData.size - 1){
                                            if (listData[i].name == ""){
                                                listData[i] = getYoutube(listData[i])
                                            }
                                        }
                                        withContext(Dispatchers.Main){
                                            adapter.notifyDataSetChanged()
                                        }
                                    }
                                    db.collection("DList")
                                        .whereLessThanOrEqualTo("date", date)
                                        .orderBy("date",Query.Direction.DESCENDING).limit(1).get()
                                        .addOnSuccessListener { result ->
                                            CoroutineScope(Dispatchers.IO).launch {
                                                for (document in result){
                                                    var item = DList(document.id, "",0,0,"",0,"",0,
                                                        "",0,0,0,0,0,0,0,0,
                                                        0,0,document["vdeioID"] as String,"",3)
                                                    val index = listData.indexOfFirst { it.id == item.id }
                                                    if (index >= 0 ) {
                                                        listData[index].flag = 3
                                                    }else{
                                                            item = getYoutube(item)
                                                            listData.add(0,item)
                                                        }
                                                    }
                                                var tempList = listData.sortedByDescending { it.flag }
                                                listData.clear()
                                                listData.addAll(tempList)
                                                withContext(Dispatchers.Main){
                                                    adapter.notifyDataSetChanged()
                                                    binding.pbList.visibility = View.INVISIBLE
                                                }
                                            }
                                        }.addOnFailureListener {
                                            Log.d("FireStore","New Fail")
                                        }
                                }
                            }
                    }
                    if(dlist.size == 0){
                        db.collection("DList")
                            .whereLessThanOrEqualTo("date", date)
                            .orderBy("date",Query.Direction.DESCENDING).limit(1).get()
                            .addOnSuccessListener { result ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    for (document in result){
                                        var item = DList(document.id, "",0,0,"",0,"",0,
                                            "",0,0,0,0,0,0,0,0,
                                            0,0,document["vdeioID"] as String,"",3)
                                        val index = listData.indexOfFirst { it.id == item.id }
                                        if (index >= 0) {
                                            listData[index].flag = 3
                                        }else{

                                                item = getYoutube(item)
                                                listData.removeIf { it.video == item.video }
                                                listData.add(0,item)

                                            }
                                        }
                                    val tempList = listData.sortedByDescending { it.flag }
                                    listData.clear()
                                    listData.addAll(tempList)
                                    withContext(Dispatchers.Main){
                                        adapter.notifyDataSetChanged()
                                        binding.pbList.visibility = View.INVISIBLE
                                    }
                                }
                            }.addOnFailureListener {
                                Log.d("FireStore","New Fail")
                            }
                    }

                }
            }
}
suspend  fun searchYoutube(searchString: String): ArrayList<DList> {
    //Daum
    var search = ""
    var dlist: ArrayList<DList>? = arrayListOf<DList>()

    try {
        search = URLEncoder.encode(searchString ,"UTF-8")
    }catch (e: UnsupportedEncodingException){
        return dlist!!
    }
    val langCode = UtilFuncs().getLanguage()
    var apiURL: String
    val requestHeader = HashMap<String, String>()
    requestHeader.put("Authorization", "KakaoAK c1cdf12b34171fae148670967c9a50e6")

    if  (langCode == "ko") {
        apiURL = "https://dapi.kakao.com/v2/search/vclip?sort=accuracy&size=5&query=" + search + "+슬기로운식샤생활" + "&cp=7b479cb3&p=1"
        dlist = searchData().get(apiURL,requestHeader, dlist!!)
        apiURL = "https://dapi.kakao.com/v2/search/vclip?sort=accuracy&size=7&query=" + search + "&cp=7b479cb3&p=1"
        dlist = searchData().get(apiURL,requestHeader, dlist!!)
    }else{
        apiURL = "https://dapi.kakao.com/v2/search/vclip?sort=accuracy&size=7&query=" + search + "&cp=7b479cb3&p=1"
        dlist = searchData().get(apiURL,requestHeader, dlist!!)
    }
    val list = dlist?.distinctBy { it.video }
    dlist?.clear()
    if (list != null){
        dlist!!.addAll(list)
    }
    Log.d("DList",dlist?.size.toString())
    return dlist
}

class searchData(){
    fun get(apiURL: String, requestHeader: HashMap<String, String>, dlist: ArrayList<DList>): ArrayList<DList> {
        val con = connect(apiURL)
        var dlist = dlist
        try{
            if (con == null){
                throw  IOException("No con")
            }
            con.requestMethod = "GET"
            for (header: Map.Entry<String, String> in requestHeader.entries) {
                con.setRequestProperty(header.key,header.value)
            }

            var responseCode: Int = con.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return readBody(con.inputStream, dlist)
            }else{
                Log.d("DaumAPI","Fail")
                return readBody(con.inputStream, dlist)
            }
        } catch (e: IOException){
            Log.d("DaumAPI","Get_IO")
            return dlist
        }finally {
            con?.disconnect()
        }
    }

    fun readBody(body: InputStream?, dlist: ArrayList<DList>): ArrayList<DList> {
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
                            val index = dlist?.indexOfFirst { it.video == url }
                            if (index == null|| index < 0){
                                var item: DList = DList("","",0,0,"",0,"",0,
                                    "",0,0,0,0,0,0,0,0,
                                    0,0,url,"",0)
                                item = getYoutube(item)
                                item = getFireStore(item)
                                if (item.id != ""){
                                    item.flag = 1
                                }
                                dlist.add(item)
                            }
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

    fun getFireStore(item: DList): DList {
        val db = FirebaseFirestore.getInstance()
        db.collection("DList").whereEqualTo("vdeioID", item.video).limit(1)
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
                }
            }
        return item
    }

   fun getYoutube(item: DList): DList {
        var item = item
        val api_key = "AIzaSyA_Q3BlyHXBGxApWzSPsHKmhLN89-FO_T8"
        val langCode = UtilFuncs().getLanguage()
        val searchList = listOf("id", "snippet")
        val id = item.video
        val idList = listOf(id)

        try {
            var httpTransport = NetHttpTransport()
//        val jsonFactory = JsonFactory()
            val jsonFactory = JacksonFactory()
            var youtube = YouTube.Builder(httpTransport, jsonFactory, HttpRequestInitializer {
                fun initialize(request: HttpRequest) {
                }
            }).setApplicationName("thethskitchen_git_ver2021072601").build()

            var video = youtube.videos().list(searchList)
            video.setKey(api_key)
            video.setHl(langCode)
            video.setId(idList)
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
            return item
        }
    }

    fun connect(apiURL: String): HttpURLConnection? {
        try {
            val url = URL(apiURL)
            return url.openConnection() as HttpURLConnection
        } catch (e: MalformedURLException) {
            Log.d("DaumAPI", "Connect_malformeURL")
            return null
        } catch (e: IOException) {
            Log.d("DaumAPI", "Connect_IO")
            return null
        }
    }


}
