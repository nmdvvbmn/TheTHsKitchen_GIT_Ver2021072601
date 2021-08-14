package com.ths.thethskitchen_git_ver2021072601

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.ths.thethskitchen_git_ver2021072601.databinding.ActivityFavoritesBinding

//즐겨찾기
class FavoritesActivity : BaseActivity() {
    val binding by lazy { ActivityFavoritesBinding.inflate(layoutInflater) }
    var adapter = FavoritesAtapter()
    private var dbHelper = SQLiteDBHelper(this,"THsKitchen.db", 1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        adapter.helper = dbHelper
        adapter.dList.addAll(dbHelper.selectFavorites())
        binding.viewFavoritesList.adapter = adapter
        binding.viewFavoritesList.layoutManager = LinearLayoutManager(this)

        //종료
        binding.btnFExit.setOnClickListener {
            finish()
        }

    }
}