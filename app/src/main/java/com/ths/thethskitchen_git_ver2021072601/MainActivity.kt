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
    val adapter = RecyclerAdapter(dlist)


    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

//        adapter.helper = helper
        binding.recyclerDList.adapter = adapter
        binding.recyclerDList.layoutManager = LinearLayoutManager(baseContext)

        db.collection("DList").get().addOnSuccessListener { result ->
            dlist.clear()
            for (document in result) {
                var item = DList(document.id as String,
                    document["date"] as Long,
                    document["pretime"] as Long,
                    document["pretimeunit"] as String,
                    document["time"] as Long,
                    document["timeunit"] as String,
                    document["qunt"] as Long,
                    document["quntunit"] as String,
                    document["video"] as String
                )
                dlist.add(item)
            }
            adapter.notifyDataSetChanged()
        }.addOnFailureListener { exception ->
            Log.d("test","fail", exception)
        }

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

