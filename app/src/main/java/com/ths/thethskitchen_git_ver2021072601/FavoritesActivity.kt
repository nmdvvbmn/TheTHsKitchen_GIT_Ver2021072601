package com.ths.thethskitchen_git_ver2021072601

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.ths.thethskitchen_git_ver2021072601.databinding.ActivityFavoritesBinding

class FavoritesActivity : AppCompatActivity() {
    val binding by lazy { ActivityFavoritesBinding.inflate(layoutInflater) }
    var adapter = FavoritesAtapter()
    var dbHelper = SQLiteDBHelper(this,"THsKitchen.db", 1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        adapter.helper = dbHelper
        adapter.dList.addAll(dbHelper?.select_favorites())
        binding.viewFavoritesList.adapter = adapter
        binding.viewFavoritesList.layoutManager = LinearLayoutManager(this)

    }
}