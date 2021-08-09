package com.ths.thethskitchen_git_ver2021072601

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ths.thethskitchen_git_ver2021072601.databinding.ActivityListBinding
import kotlinx.coroutines.*
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.net.URLEncoder

class ListActivity : AppCompatActivity() {
    val binding by lazy { ActivityListBinding.inflate(layoutInflater)}
    var dlist: ArrayList<DList>? = arrayListOf()
    lateinit var adapter: RecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val search = intent.getStringExtra("search") as String

        binding.searchDList.layoutManager = LinearLayoutManager(this)
        getData(search)
    }

    private fun getData(search: String) {

        CoroutineScope(Dispatchers.Main).launch {
            binding.pbList.visibility = View.VISIBLE

            withContext(Dispatchers.IO) {
                dlist = searchYoutube(search)
            }

            if (dlist != null) {
                adapter = RecyclerAdapter(dlist!!)
                binding.searchDList.adapter = adapter
            }
            binding.pbList.visibility = View.INVISIBLE
            adapter.notifyDataSetChanged()
        }
    }

//    fun searchYoutube(searchString: String): ArrayList<DList> {
//        //네이버
//        var search: String? = null
//        var dlist = arrayListOf<DList>()
//        try {
//            search = URLEncoder.encode(searchString + "youtube","UTF-8")
//            Log.d("NaverAPI","search : ${search}")
//        }catch (e: UnsupportedEncodingException){
//            val item = DList("","None",0,0,"",0,"",0,
//                "",0,0,0,0,0,0,0,0,
//                0,0,"","")
//            dlist.add(item)
//            return dlist
//        }
////    val address = "https://dapi.kakao.com/v2/search/vclip?sort=accuracy&size=1=&query=" +
////            searchString + "+youtube"
////    val apiURL = "https://openapi.naver.com/v1/search/image?query=" + search
//        val apiURL = "https://dapi.kakao.com/v2/search/vclip?sort=accuracy&size=1=&query=" + search
//        Log.d("NaverAPI","apiURL : ${apiURL}")
//        val requestHeader = HashMap<String, String>()
////    requestHeader.put("X-Naver-Client-Id", "IIG3vZ0a4SjAoRcjA1Rk")
////    requestHeader.put("X-Naver-Client-Secret", "eagRBo5Jta")
//        requestHeader.put("Authorization", "KakaoAK c1cdf12b34171fae148670967c9a50e6")
//        Log.d("NaverAPI","requestHeader : ${requestHeader}")
//        val responseBody = get(apiURL,requestHeader);
//        Log.d("NaverAPI","${responseBody}")
//        val item = DList("","None",0,0,"",0,"",0,
//            "",0,0,0,0,0,0,0,0,
//            0,0,"",responseBody)
//        dlist.add(item)
//        Log.d("NaverAPI","${responseBody}")
//        return dlist
//    }
//
//    fun get(apiURL: String, requestHeader: HashMap<String, String>): String {
//        val con = connect(apiURL)
//        Log.d("NaverAPI","con : ${con}")
//        try{
//            if (con == null){
//                Log.d("NaverAPI","No con")
//                throw  IOException("No con")
//            }
//            con?.requestMethod = "GET"
//            for (header: Map.Entry<String, String> in requestHeader.entries) {
//                con?.setRequestProperty(header.key,header.value)
//                Log.d("NaverAPI","setHeadre")
//            }
//            var responseCode: Int = con!!.responseCode
//            Log.d("NaverAPI","responseCode : ${responseCode}")
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//
//                Log.d("NaverAPI","con  : ${con.contentEncoding}")
//                return readBody(con.inputStream)
//            }else{
//                Log.d("NaverAPI","Fail")
//                return readBody(con.inputStream)
//            }
//        } catch (e: IOException){
//            Log.d("NaverAPI","Get_IO")
//            return "Error"
//        }finally {
//            con?.disconnect()
//        }
//    }
//
//    fun readBody(body: InputStream?): String {
//        val streamReader = InputStreamReader(body)
//        Log.d("NaverAPI","body : ${body}")
//        Log.d("NaverAPI","streaReader : ${streamReader}")
//        try {
//            var lineReader = BufferedReader(streamReader)
//            var responseBody = StringBuilder()
//            while ((lineReader.readLine()) != null) {
//                responseBody.append(lineReader.readLine())
//                Log.d("NaverAPI","readLine : ${lineReader.readLine()}")
//            }
//            return  responseBody.toString()
//        }catch (e: IOException){
//            Log.d("NaverAPI","Readbody IO")
//            return "Error"
//        }
//    }
//
//    fun connect(apiURL: String): HttpURLConnection? {
//        try {
//            val url = URL(apiURL)
//            return url.openConnection() as HttpURLConnection
//        } catch (e: MalformedURLException) {
//            Log.d("NaverAPI", "Connect_malformeURL")
//            return null
//        } catch (e: IOException) {
//            Log.d("NaverAPI", "Connect_IO")
//            return null
//        }
//    }

}

