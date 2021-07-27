package com.ths.thethskitchen_git_ver2021072601

import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import com.ths.a20210713_sqllitehelper.*
import com.ths.thethskitchen_git_ver2021072601.databinding.ActivityMainBinding
import java.io.IOException


//suspend fun downloadList(inputStream: InputStream?, range: String, )
//        : Pair<List<List<DList>>?, List<List<IList>>?> {
//
//    val ( dlist, ilist ) = GoogleDriveService()
//        .getDlist("1sLZ37OjOlzRHzPnzp-r7eO8xcffmajwRFfbbIQTVsKU", inputStream, range )
//    return  Pair(dlist, ilist) //sdkf
//}

class MainActivity : AppCompatActivity() {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater)}
//    val helper = SqliteHelper(this,"dlist",1)
    val db = FirebaseFirestore.getInstance()
    val dlist = arrayListOf<DList>()


    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val adapter = RecyclerAdapter()
//        adapter.helper = helper
        binding.recyclerDList.adapter = adapter
        binding.recyclerDList.layoutManager = LinearLayoutManager(baseContext)
        Log.d("test","${isOnline()}")
        db.collection("Dlist").get().addOnSuccessListener { result ->
            dlist.clear()
            Log.d("test","test")
            for (document in result) {
                Log.d("test","getdocument")
                Log.d("test","${document.data}")
            }
        }.addOnFailureListener { exception ->
            Log.d("test","fail", exception)
        }
        var data = db.collection("Dlist").document("00001").get()
        Log.d("test","test")

//        CoroutineScope(Dispatchers.Main).launch {
//            val inputStream = assets?.open("thethskitchen-46bad73155c3.json")
//            val dRange = (helper.selectIRangeMin().toLong() + 4).toString()
//            val ( dlist, ilist ) = downloadList(inputStream, dRange)
//            helper.transDList(dlist)
//            helper.transIList(ilist)

//            adapter.listData.addAll(helper.selectDlist())
//        }
    }
    fun isOnline(): Boolean {
        val runtime = Runtime.getRuntime()
        try {
            val ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8")
            val exitValue = ipProcess.waitFor()
            return exitValue == 0
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return false
    }


}

