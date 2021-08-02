package com.ths.thethskitchen_git_ver2021072601

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker
import com.ths.thethskitchen_git_ver2021072601.databinding.ActivityRecommandDetailBinding

var tracker = YouTubePlayerTracker()
var mYoutubePlayer: YouTubePlayer? = null

class RecommandDetail : AppCompatActivity() {
    val binding by lazy { ActivityRecommandDetailBinding.inflate(layoutInflater) }
    val helper =  SQLiteDBHelper(this,"THsKitchen.db", 1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val dlist = intent.getSerializableExtra("data") as DList
        val link = "https://youtu.be/${dlist.video}"
        getLifecycle().addObserver(binding.youtubeView)

        //var youtubeListener = AbYoutubePlayerListener(dlist.link, 30f)
        binding.youtubeView.addYouTubePlayerListener(AbYoutubePlayerListener("${dlist.video}", 0f))

        val adapter = IListAdapter()
//        adapter.helper = helper
        binding.recycleIList.adapter = adapter
        binding.recycleIList.layoutManager = LinearLayoutManager(this)

//        adapter.listData.addAll(helper.selectIlist(dlist.id))

        var savedFavorites = helper.exists_favorites(dlist.id)
        if (savedFavorites) {
            binding.btnFavorites.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_baseline_star_24,0,0,0)
        } else {
            binding.btnFavorites.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_baseline_star_border_24,0,0,0)
        }

        binding.btnMove.setOnClickListener {
            var second = tracker.currentSecond
            var stat = tracker.state
            Log.d("YOUTUBE_STAT","${stat} : ${second}")
            mYoutubePlayer?.seekTo(second+10f)
        }

        binding.btnFavorites.setOnClickListener {
            if (savedFavorites) {
                helper.delete_favorites(dlist)
                binding.btnFavorites.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_baseline_star_border_24,0,0,0)
                savedFavorites = false
            }else{
                helper.insert_favorites(dlist)
                binding.btnFavorites.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_baseline_star_24,0,0,0)
                savedFavorites = true
            }

        }
    }
}

class AbYoutubePlayerListener(videoId: String, second: Float): AbstractYouTubePlayerListener() {
    val videoId = videoId
    var second = second
    override fun onReady(youTubePlayer: YouTubePlayer){
        youTubePlayer.loadVideo(videoId, 0f)
        youTubePlayer.addListener(tracker)
        mYoutubePlayer = youTubePlayer
    }

    override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
        super.onCurrentSecond(youTubePlayer, second)
    }
}

