package com.ths.thethskitchen_git_ver2021072601

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.api.client.http.HttpRequest
import com.google.api.client.http.HttpRequestInitializer
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.SearchResult
import com.google.firebase.firestore.FirebaseFirestore
import com.ths.thethskitchen_git_ver2021072601.databinding.ActivityListBinding
import com.ths.thethskitchen_git_ver2021072601.databinding.ActivityMainBinding
import java.io.IOException
import java.lang.Exception

class ListActivity : AppCompatActivity() {
    val binding by lazy { ActivityListBinding.inflate(layoutInflater)}
//    val db = FirebaseFirestore.getInstance()
    val dlist = arrayListOf<DList>()
    val adapter = RecyclerAdapter(dlist)
//    val langCode = UtilFuncs().getLanguage()    // 언어코드
    val api_key = "AIzaSyA_Q3BlyHXBGxApWzSPsHKmhLN89-FO_T8"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val search = intent.getStringExtra("search") as String

        binding.searchDList.adapter = adapter
        binding.searchDList.layoutManager = LinearLayoutManager(baseContext)

        youtubeSearch()
    }


    private fun youtubeSearch() {
        try {
            val http_transport = NetHttpTransport()
            val json_factory = JacksonFactory()
            val number_of_videos_retured: Long = 5
            val httpRequest = HttpRequestInitializer() {
                fun initialize(request: HttpRequest) {
                }
            }
            val youtube = YouTube.Builder(http_transport, json_factory, httpRequest)
                .setApplicationName("youtube_search").build()
            var partList = listOf<String>("id", "snippet")
            val search = youtube.search().list(partList)
            search.setKey(api_key)
            search.setChannelId("UCM_NeHV1e_QZ5dYr-C-TJqA")
            search.setOrder("relevance")
            val typeList = listOf("video")
            search.setType(typeList)
            search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)")
            search.setMaxResults(number_of_videos_retured)
            var searchResponse = search.execute()

            var searchResult = searchResponse.items

            if (searchResult != null){
                while (searchResult.iterator().hasNext()){
                    var singleVideo = searchResult.iterator().next()
                    Log.d("Title", "${singleVideo.getSnippet().title}")
                }
            }
        }catch (e: Exception){

        }
    }

}