package com.ths.thethskitchen_git_ver2021072601

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ths.thethskitchen_git_ver2021072601.databinding.ActivityListBinding
import kotlinx.coroutines.*

class ListActivity : AppCompatActivity() {
    val binding by lazy { ActivityListBinding.inflate(layoutInflater)}
    var dlist = arrayListOf<DList>()
    var adapter = RecyclerAdapter(dlist)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val search = intent.getStringExtra("search") as String
        binding.searchDList.adapter = adapter
        binding.searchDList.layoutManager = LinearLayoutManager(this)
        getData(search)
    }

    private fun getData(search: String) {
        dlist.clear()
        binding.pbList.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                dlist.addAll(searchYoutube(search))
                searchFireStore(search, adapter, dlist, binding)
            }

        }
    }
}

