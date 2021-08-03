package com.ths.thethskitchen_git_ver2021072601

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewDebug
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker
import com.ths.thethskitchen_git_ver2021072601.databinding.ActivityRecommandDetailBinding

var tracker = YouTubePlayerTracker()
var mYoutubePlayer: YouTubePlayer? = null

class RecommandDetail : AppCompatActivity() {
    val binding by lazy { ActivityRecommandDetailBinding.inflate(layoutInflater) }
    val helper =  SQLiteDBHelper(this,"THsKitchen.db", 1)
    var iList = arrayListOf<IList>()
    var iAdapter = IListAdapter(iList)
    val db = FirebaseFirestore.getInstance()
    var oldQunt : Float = 0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        var dlist = intent.getSerializableExtra("data") as DList
        val link = "https://youtu.be/${dlist.video}"
        getLifecycle().addObserver(binding.youtubeView)

        //var youtubeListener = AbYoutubePlayerListener(dlist.link, 30f)
        binding.youtubeView.addYouTubePlayerListener(AbYoutubePlayerListener("${dlist.video}", dlist.start.toFloat()))

        binding.recycleIList.adapter = iAdapter
        binding.recycleIList.layoutManager = LinearLayoutManager(this)
        selectIList(dlist)

        var savedFavorites = helper.exists_favorites(dlist.id)
        setFavoritesButton(savedFavorites)  // 즐겨찾기 버튼

        oldQunt = dlist.qunt.toFloat()
        binding.editQunt.setText(UtilFuncs().floatFormat(oldQunt))
        binding.txtUnit.setText(UtilFuncs().transUnit(dlist.quntunit))

        oldQunt = setItemQunt(oldQunt, binding.editQunt.text.toString().toFloat())

        binding.btnEnter.setOnClickListener {
            oldQunt = setItemQunt(oldQunt, binding.editQunt.text.toString().toFloat())
        }

        binding.txtPtime.setText( dlist.pretime.toString() + " " +
                getString(UtilFuncs().transUnit(dlist.preunit)))
        binding.txtCtime.setText( dlist.time.toString() + " " +
                getString(UtilFuncs().transUnit(dlist.timeunit)))
        if (dlist.pretime <= 0){
            binding.txtPtime.visibility = View.INVISIBLE
            binding.txtPTEXT.visibility = View.INVISIBLE
        }
        var toolTxt : String = ""
        if (dlist.stove > 0 || dlist.oven > 0 || dlist.micro > 0 || dlist.blender > 0 ||
            dlist.airfryer > 0 || dlist.multi > 0 || dlist.steamer > 0 || dlist.sousvide > 0 ||
            dlist.grill > 0 ) {
            toolTxt = this.getString(R.string.tool) + " "
            if (dlist.stove > 0) {
                toolTxt = toolTxt + this.getString(R.string.stove) + " "
            }

            binding.txtTool.setText(toolTxt)
        }else{
            binding.txtTool.visibility = View.INVISIBLE
        }

//        binding.btnMove.setOnClickListener {
//            var second = tracker.currentSecond
//            var stat = tracker.state
//            Log.d("YOUTUBE_STAT","${stat} : ${second}")
//            mYoutubePlayer?.seekTo(second+10f)
//        }

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

    private fun setItemQunt(oldQunt: Float, newQunt: Float ) : Float {
        var qunt = 0f
        if (newQunt <= 0f)
        {
            binding.editQunt.setText(UtilFuncs().floatFormat(oldQunt))
            return  oldQunt
        }else{
            qunt = newQunt / oldQunt
        }


        for (index in 0 until  iList.size - 1 ) {
            iList[index].qunt = iList[index].qunt * qunt
        }
        iAdapter.notifyDataSetChanged()

        return  newQunt
    }

    private fun selectIList(dList: DList) {
        //         firestore 요리 리스트 Query
        val langCode = UtilFuncs().getLanguage()
        iList.clear()
        db.collection("IList")
            .whereEqualTo("id", dList.id)
            .get().addOnSuccessListener {  result ->
                for (document in result) {
                    var item = IList(
                        document["id"] as String,
                        document.id,
                        "",
                        "${document["qunt"]}".toFloat() ,
                        document["unit"] as String,
                        document["essential"] as Boolean )

                    db.collection("IName")
                        .whereEqualTo("SEQ", document.id)
                        .whereEqualTo("code", langCode)
                        .get().addOnSuccessListener { result ->
                            for (document in result) {
                                var index = iList.indexOfFirst{
                                    it.SEQ == document["SEQ"] as String }
                                iList[index].name = document["name"] as String
                            }
                        }.addOnFailureListener {

                        }.addOnCompleteListener {
                            iAdapter.notifyDataSetChanged()
                        }
                    iList.add(item)
                }
            }.addOnFailureListener {

            }.addOnCompleteListener {

            }

    }

    fun setFavoritesButton(savedFavorites: Boolean) {
        if (savedFavorites) {
            binding.btnFavorites.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_baseline_star_24,0,0,0)
        } else {
            binding.btnFavorites.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_baseline_star_border_24,0,0,0)
        }
    }
}

class AbYoutubePlayerListener(videoId: String, second: Float): AbstractYouTubePlayerListener() {
    val videoId = videoId
    var second = second
    override fun onReady(youTubePlayer: YouTubePlayer){
        youTubePlayer.loadVideo(videoId, second)
        youTubePlayer.addListener(tracker)
        mYoutubePlayer = youTubePlayer
    }

    override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
        super.onCurrentSecond(youTubePlayer, second)
    }
}

