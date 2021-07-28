package com.ths.thethskitchen_git_ver2021072601

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.ths.thethskitchen_git_ver2021072601.databinding.ActivityListBinding
import com.ths.thethskitchen_git_ver2021072601.databinding.ActivityMainBinding
import java.io.IOException

class ListActivity : AppCompatActivity() {
    val binding by lazy { ActivityListBinding.inflate(layoutInflater)}
    val db = FirebaseFirestore.getInstance()
    val dlist = arrayListOf<DList>()
    val adapter = RecyclerAdapter(dlist)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val search = intent.getStringExtra("search") as String

        Log.d("search",search)

        binding.searchDList.adapter = adapter
        binding.searchDList.layoutManager = LinearLayoutManager(baseContext)


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