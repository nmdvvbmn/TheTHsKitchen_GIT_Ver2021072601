package com.ths.a20210713_sqllitehelper

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
//askdjflkjlkjlkjalskdjlfkjlkajsdfdfdd
class RecommandDetail : AppCompatActivity() {
    val binding by lazy { ActivityRecommandDetailBinding.inflate(layoutInflater) }
    val helper = SqliteHelper(this,"dlist",1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val dlist = intent.getSerializableExtra("data") as DList
        getLifecycle().addObserver(binding.youtubeView)

        //var youtubeListener = AbYoutubePlayerListener(dlist.link, 30f)
        binding.youtubeView.addYouTubePlayerListener(AbYoutubePlayerListener(dlist.link, 0f))

        val adapter = IListAdapter()
        adapter.helper = helper
        binding.recycleIList.adapter = adapter
        binding.recycleIList.layoutManager = LinearLayoutManager(this)

        adapter.listData.addAll(helper.selectIlist(dlist.no))

        binding.btnMove.setOnClickListener {
            var second = tracker.currentSecond
            var stat = tracker.state
            Log.d("YOUTUBE_STAT","${stat} : ${second}")
            mYoutubePlayer?.seekTo(second+10f)
//
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