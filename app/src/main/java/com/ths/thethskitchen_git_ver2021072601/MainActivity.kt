package com.ths.thethskitchen_git_ver2021072601

import com.ths.thethskitchen_git_ver2021072601.databinding.ActivityMainBinding

import android.os.Bundle
import android.os.StrictMode
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ths.a20210713_sqllitehelper.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStream


suspend fun downloadList(inputStream: InputStream?, range: String, )
        : Pair<List<List<DList>>?, List<List<IList>>?> {

    val ( dlist, ilist ) = GoogleDriveService()
        .getDlist("1sLZ37OjOlzRHzPnzp-r7eO8xcffmajwRFfbbIQTVsKU", inputStream, range )
    return  Pair(dlist, ilist)
}
// git Test1zxsdfsdf

class MainActivity : AppCompatActivity() {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater)}
    val helper = SqliteHelper(this,"dlist",1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val adapter = RecyclerAdapter()
        adapter.helper = helper
        binding.recyclerDList.adapter = adapter
        binding.recyclerDList.layoutManager = LinearLayoutManager(baseContext)


        CoroutineScope(Dispatchers.Main).launch {
            val inputStream = assets?.open("thethskitchen-46bad73155c3.json")
            val dRange = (helper.selectIRangeMin().toLong() + 4).toString()
            val ( dlist, ilist ) = downloadList(inputStream, dRange)
            helper.transDList(dlist)
            helper.transIList(ilist)

            adapter.listData.addAll(helper.selectDlist())
        }
    }




}

