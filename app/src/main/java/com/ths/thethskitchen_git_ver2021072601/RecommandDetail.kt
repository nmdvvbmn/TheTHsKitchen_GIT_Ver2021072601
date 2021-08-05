package com.ths.thethskitchen_git_ver2021072601

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
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

class RecommandDetail : AppCompatActivity(), RListAdapter.OnRItem {
    val binding by lazy { ActivityRecommandDetailBinding.inflate(layoutInflater) }
    val helper =  SQLiteDBHelper(this,"THsKitchen.db", 1)
    val iList = arrayListOf<IList>()
    var iAdapter = IListAdapter(iList)
    val rList = arrayListOf<RList>()
    val rAdapter = RListAdapter(rList,this)
    val db = FirebaseFirestore.getInstance()
    var oldQunt : Float = 0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        var dlist = intent.getSerializableExtra("data") as DList
        val link = "https://youtu.be/${dlist.video}"
        getLifecycle().addObserver(binding.youtubeView)

        binding.youtubeView.addYouTubePlayerListener(AbYoutubePlayerListener("${dlist.video}", dlist.start.toFloat()))

        iAdapter.dlist = dlist
        binding.recycleIList.adapter = iAdapter
        binding.recycleIList.layoutManager = LinearLayoutManager(this)
        selectIList(dlist)

        binding.recycleRList.adapter = rAdapter
        binding.recycleRList.layoutManager = LinearLayoutManager(this)
        selectRList(dlist)

        var savedFavorites = helper.exists_favorites(dlist.id)
        setFavoritesButton(savedFavorites)  // 즐겨찾기 버튼

        oldQunt = dlist.qunt.toFloat()
        binding.editQunt.setText(UtilFuncs().floatFormat(oldQunt))
        binding.txtUnit.setText(UtilFuncs().transUnit(dlist.quntunit))

        oldQunt = setItemQunt(oldQunt, binding.editQunt.text.toString().toFloat())

        binding.txtRTitle.text = dlist.name
        binding.txtPtime.setText( dlist.pretime.toString() + " " +
                getString(UtilFuncs().transUnit(dlist.preunit)))
        binding.txtCtime.setText( dlist.time.toString() + " " +
                getString(UtilFuncs().transUnit(dlist.timeunit)))
        if (dlist.pretime <= 0){
            binding.txtPtime.visibility = View.INVISIBLE
            binding.txtPTEXT.visibility = View.INVISIBLE
        }
        setTool(dlist)

//        binding.btnMove.setOnClickListener {
//            var second = tracker.currentSecond
//            var stat = tracker.state
//            Log.d("YOUTUBE_STAT","${stat} : ${second}")
//            mYoutubePlayer?.seekTo(second+10f)
//        }

        binding.btnEnter.setOnClickListener {
            oldQunt = setItemQunt(oldQunt, binding.editQunt.text.toString().toFloat())
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

        binding.btnShare.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.setType("text/plain")
            val text = dlist.name + "\n" +
                    "https://www.youtube.com/watch?v=" + dlist.video
            intent.putExtra(Intent.EXTRA_TEXT,text)
            val chooser = Intent.createChooser(intent, this.getString(R.string.title_share))
            startActivity(chooser);
        }

        binding.btnRLink.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.youtube.com/watch?v=" + dlist.video))
            startActivity(intent)
        }
    }

    private fun setTool(dlist: DList) {
        var toolTxt : String = ""
        if (dlist.stove > 0 || dlist.oven > 0 || dlist.micro > 0 || dlist.blender > 0 ||
            dlist.airfryer > 0 || dlist.multi > 0 || dlist.steamer > 0 || dlist.sousvide > 0 ||
            dlist.grill > 0 ) {
            toolTxt = this.getString(R.string.tool) + "\n"
            if (dlist.stove > 0) {
                toolTxt = toolTxt + this.getString(R.string.stove)
                if (dlist.stove < 2) toolTxt = toolTxt + this.getString(R.string.option) + "\n"
                else toolTxt = toolTxt + "\n"
            }
            if (dlist.oven > 0) {
                toolTxt = toolTxt + this.getString(R.string.oven)
                if (dlist.oven < 2) toolTxt = toolTxt + this.getString(R.string.option) + "\n"
                else toolTxt = toolTxt + "\n"
            }
            if (dlist.micro > 0) {
                toolTxt = toolTxt + this.getString(R.string.micro)
                if (dlist.micro < 2) toolTxt = toolTxt + this.getString(R.string.option) + "\n"
                else toolTxt = toolTxt + "\n"
            }
            if (dlist.blender > 0) {
                toolTxt = toolTxt + this.getString(R.string.blender)
                if (dlist.blender < 2) toolTxt = toolTxt + this.getString(R.string.option) + "\n"
                else toolTxt = toolTxt + "\n"
            }
            if (dlist.airfryer > 0) {
                toolTxt = toolTxt + this.getString(R.string.airfryer)
                if (dlist.airfryer < 2) toolTxt = toolTxt + this.getString(R.string.option) + "\n"
                else toolTxt = toolTxt + "\n"
            }
            if (dlist.multi > 0) {
                toolTxt = toolTxt + this.getString(R.string.multi)
                if (dlist.multi < 2) toolTxt = toolTxt + this.getString(R.string.option) + "\n"
                else toolTxt = toolTxt + "\n"
            }
            if (dlist.steamer > 0) {
                toolTxt = toolTxt + this.getString(R.string.steam)
                if (dlist.steamer < 2) toolTxt = toolTxt + this.getString(R.string.option) + "\n"
                else toolTxt = toolTxt + "\n"
            }
            if (dlist.sousvide > 0) {
                toolTxt = toolTxt + this.getString(R.string.sous)
                if (dlist.sousvide < 2) toolTxt = toolTxt + this.getString(R.string.option) + "\n"
                else toolTxt = toolTxt + "\n"
            }
            if (dlist.grill > 0) {
                toolTxt = toolTxt + this.getString(R.string.grill)
                if (dlist.grill < 2) toolTxt = toolTxt + this.getString(R.string.option) + "\n"
                else toolTxt = toolTxt + "\n"
            }
            binding.txtTool.setText(toolTxt)
        }else{
            binding.txtTool.visibility = View.INVISIBLE
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
        binding.pbIlist.visibility = View.VISIBLE
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
                            binding.pbIlist.visibility = View.INVISIBLE
                        }
                    iList.add(item)
                }
            }.addOnFailureListener {

            }.addOnCompleteListener {

            }
    }

    private fun selectRList(dList: DList) {
        //         firestore 요리 리스트 Query
        val langCode = UtilFuncs().getLanguage()
        rList.clear()
        binding.pbRlist.visibility = View.VISIBLE
        db.collection("RList")//.orderBy("seq")
            .whereEqualTo("id", dList.id)
            .get().addOnSuccessListener {  result ->
                for (document in result) {
                    var item = RList(
                        document["id"] as String,
                        document["seq"] as String,
                        "",
                        "${document["time"]}".toFloat()  )

                    db.collection("RName")
                        .whereEqualTo("SEQ", document["seq"] as String)
                        .whereEqualTo("code", langCode)
                        .get().addOnSuccessListener { result ->
                            for (document in result) {
                                var index = rList.indexOfFirst{
                                    it.SEQ == document["SEQ"] as String }
                                rList[index].name = document["name"] as String
                            }
                        }.addOnFailureListener {
                            Log.d("RName","Fail")
                        }.addOnCompleteListener {
                            rAdapter.notifyDataSetChanged()
                            binding.pbRlist.visibility = View.INVISIBLE
                        }
                    rList.add(item)
                }
            }.addOnFailureListener {
                Log.d("RList","Fail")
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

    override fun onRItemSelected(time: Float) {
        mYoutubePlayer?.seekTo(time)
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

