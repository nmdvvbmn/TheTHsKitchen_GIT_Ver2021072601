package com.ths.thethskitchen_git_ver2021072601

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.ths.thethskitchen_git_ver2021072601.databinding.ActivityListBinding
import kotlinx.coroutines.*

class ListActivity : BaseActivity() {
    val binding by lazy { ActivityListBinding.inflate(layoutInflater)}
    var dlist = arrayListOf<DList>()
    var adapter = RecyclerAdapter(dlist,1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val search = intent.getStringExtra("search") as String
        binding.searchDList.adapter = adapter
        binding.searchDList.layoutManager = LinearLayoutManager(this)
        binding.searchDList.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        val animator = binding.searchDList.itemAnimator
        if (animator is SimpleItemAnimator) {
            animator.supportsChangeAnimations = false
        }
        getData(search)
        binding.btmLexit.setOnClickListener{
            finish()
        }
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

