package com.ths.thethskitchen_git_ver2021072601

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewDebug
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.ths.thethskitchen_git_ver2021072601.databinding.ActivityRecommandDetailBinding

//var tracker = YouTubePlayerTracker()
var mYoutubePlayer: YouTubePlayer? = null

class RecommandDetail : AppCompatActivity(), RListAdapter.OnRItem {
    val binding by lazy { ActivityRecommandDetailBinding.inflate(layoutInflater) }
    val helper =  SQLiteDBHelper(this,"THsKitchen.db", 1)   // 장바구니, 즐겨찾기 로컬 DB
    val iList = arrayListOf<IList>()    //재료 리스트
    var iAdapter = IListAdapter(iList)  //재료 어뎁터
    val rList = arrayListOf<RList>()    //레시피
    val rAdapter = RListAdapter(rList,this) //레시피 어뎁터
    val db = FirebaseFirestore.getInstance()    // 요리ID로 파이어베이스에서 IList, IName, RList. RName
    var oldQunt : Float = 0F    // 재료량 계산을 위한 이전값 저장
    val langCode = UtilFuncs().getLanguage() // 저장된 언어

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        var dlist = intent.getSerializableExtra("data") as DList    // 이전 페이지 선택 데이터
        val link = "https://youtu.be/${dlist.video}"    // 유튜브 URL
        var caption = 1 // 캡션설정 (디폴트 자막있음)
        
        //자막 없음 설정인 경우
        if (App.prefs.getBoolean("caption",true) == false) {
            caption = 0
        }

        var option = IFramePlayerOptions.Builder().ccLoadPolicy(caption).build()    //자막설정을 위한 옵션
        var youtubePlayerListener = AbYoutubePlayerListener("${dlist.video}", dlist.start.toFloat())
        // 유튜브API 리스너

        getLifecycle().addObserver(binding.youtubeView)
        binding.youtubeView.enableAutomaticInitialization = false   // 옵션 선택시 자동초기화 false
        binding.youtubeView.initialize(youtubePlayerListener,true,option)   //자막을 위한 수동 초기화

        var savedFavorites = helper.exists_favorites(dlist.id) //즐겨찾기 저장 유무
        setFavoritesButton(savedFavorites)  // 즐겨찾기 버튼

        oldQunt = dlist.qunt.toFloat()  // 기본 양 
        binding.editQunt.setText(UtilFuncs().floatFormat(oldQunt))  //Float->Int(String)
        binding.txtUnit.setText(UtilFuncs().transUnit(dlist.quntunit))  //단위 언어에 맞게 세팅

        if (dlist.id == "") {
            binding.txtTool.visibility = View.INVISIBLE
            binding.editQunt.visibility = View.INVISIBLE
            binding.txtUnit.visibility = View.INVISIBLE
            binding.recycleIList.visibility = View.INVISIBLE
            binding.recycleRList.visibility = View.INVISIBLE
            binding.pbIlist.visibility = View.INVISIBLE
            binding.pbRlist.visibility = View.INVISIBLE
            binding.txtRDesc.text = dlist.desc

        }else{
            //재료 RecyclerView
            iAdapter.dlist = dlist
            binding.recycleIList.adapter = iAdapter
            binding.recycleIList.layoutManager = LinearLayoutManager(this)
            selectIList(dlist)

            //레시피 RecyclerView
            binding.recycleRList.adapter = rAdapter
            binding.recycleRList.layoutManager = LinearLayoutManager(this)
            selectRList(dlist)
            binding.txtRDesc.visibility = View.INVISIBLE
            setTool(dlist) //사용도구 텍스트
            oldQunt = setItemQunt(oldQunt, binding.editQunt.text.toString().toFloat())  // 사용량 계산
        }

        binding.txtRTitle.text = dlist.name //상단 요리명
        
        if (dlist.pretime <= 0){    //준비시간 없으면 비활성
            binding.txtPtime.visibility = View.INVISIBLE
            binding.txtPTEXT.visibility = View.INVISIBLE
        }else{
            binding.txtPtime.setText( dlist.pretime.toString() + " " +
                    getString(UtilFuncs().transUnit(dlist.preunit))) //준비시간 + 단위    
        }

        if (dlist.time <= 0) {
            binding.txtCtime.visibility = View.INVISIBLE
            binding.txtCTEXT.visibility = View.INVISIBLE
        }else{
            binding.txtCtime.setText( dlist.time.toString() + " " +
                    getString(UtilFuncs().transUnit(dlist.timeunit)))   //조리시간 + 단위
        }




        //즐겨찾기 버튼
        binding.btnFavorites.setOnClickListener {
            if (savedFavorites) {   //즐겨찾기 취소
                helper.delete_favorites(dlist)  //favoriate에서 삭제
                binding.btnFavorites.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_baseline_star_border_24,0,0,0)
                savedFavorites = false //저장 안됨 플래그
            }else{  //즐겨찾기 등록
                helper.insert_favorites(dlist)  //favoriate insert
                binding.btnFavorites.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_baseline_star_24,0,0,0)
                savedFavorites = true   //저장됨 플랙그
            }
        }

        // 공유버튼
        binding.btnShare.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.setType("text/plain")
            val text = dlist.name + "\n" +
                    "https://www.youtube.com/watch?v=" + dlist.video //유튜브 URL만 전달(임시)
            intent.putExtra(Intent.EXTRA_TEXT,text)
            val chooser = Intent.createChooser(intent, this.getString(R.string.title_share))
            startActivity(chooser);
        }
        
        //유튜브 채널 이동
        binding.btnRLink.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.youtube.com/watch?v=" + dlist.video))
            startActivity(intent)
        }

        // 장바구니로 이동
        binding.btnToCart.setOnClickListener {
            val intent = Intent(this,CartActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            startActivity(intent)
        }

        //종료
        binding.btnRecommandExit.setOnClickListener {
            finish()
        }

        //
        binding.editQunt.doAfterTextChanged {
            Log.d("editText","${binding.editQunt.text}")
            if (binding.editQunt.text.toString() != ""){
                oldQunt = setItemQunt(oldQunt, binding.editQunt.text.toString().toFloat())
            }
        }
    }

    //조리도구 텍스트
    private fun setTool(dlist: DList) {
        var toolTxt : String = ""
        if (dlist.stove > 0 || dlist.oven > 0 || dlist.micro > 0 || dlist.blender > 0 ||
            dlist.airfryer > 0 || dlist.multi > 0 || dlist.steamer > 0 || dlist.sousvide > 0 ||
            dlist.grill > 0 ) {
            toolTxt = this.getString(R.string.tool) + "\n"
            if (dlist.stove > 0) {  //레인지
                toolTxt = toolTxt + this.getString(R.string.stove)
                if (dlist.stove < 2) toolTxt = toolTxt + this.getString(R.string.option) + "\n"
                else toolTxt = toolTxt + "\n"
            }
            if (dlist.oven > 0) {   //오븐
                toolTxt = toolTxt + this.getString(R.string.oven)
                if (dlist.oven < 2) toolTxt = toolTxt + this.getString(R.string.option) + "\n"
                else toolTxt = toolTxt + "\n"
            }
            if (dlist.micro > 0) {  //전자레인지
                toolTxt = toolTxt + this.getString(R.string.micro)
                if (dlist.micro < 2) toolTxt = toolTxt + this.getString(R.string.option) + "\n"
                else toolTxt = toolTxt + "\n"
            }
            if (dlist.blender > 0) {    //블랜더
                toolTxt = toolTxt + this.getString(R.string.blender)
                if (dlist.blender < 2) toolTxt = toolTxt + this.getString(R.string.option) + "\n"
                else toolTxt = toolTxt + "\n"
            }
            if (dlist.airfryer > 0) {   //에어프라이어
                toolTxt = toolTxt + this.getString(R.string.airfryer)
                if (dlist.airfryer < 2) toolTxt = toolTxt + this.getString(R.string.option) + "\n"
                else toolTxt = toolTxt + "\n"
            }
            if (dlist.multi > 0) {  //인스턴트팟
                toolTxt = toolTxt + this.getString(R.string.multi)
                if (dlist.multi < 2) toolTxt = toolTxt + this.getString(R.string.option) + "\n"
                else toolTxt = toolTxt + "\n"
            }
            if (dlist.steamer > 0) {    //찜기
                toolTxt = toolTxt + this.getString(R.string.steam)
                if (dlist.steamer < 2) toolTxt = toolTxt + this.getString(R.string.option) + "\n"
                else toolTxt = toolTxt + "\n"
            }
            if (dlist.sousvide > 0) {       //수비드
                toolTxt = toolTxt + this.getString(R.string.sous)
                if (dlist.sousvide < 2) toolTxt = toolTxt + this.getString(R.string.option) + "\n"
                else toolTxt = toolTxt + "\n"
            }
            if (dlist.grill > 0) {  //그릴
                toolTxt = toolTxt + this.getString(R.string.grill)
                if (dlist.grill < 2) toolTxt = toolTxt + this.getString(R.string.option) + "\n"
                else toolTxt = toolTxt + "\n"
            }
            binding.txtTool.setText(toolTxt)
        }else{  //조리도구 없는경우
            binding.txtTool.visibility = View.INVISIBLE
        }
    }

    // 조리량에 따른 재료 량 변경
    private fun setItemQunt(oldQunt: Float, newQunt: Float ) : Float {
        var qunt = 0f   //변경 비율
        if (newQunt <= 0f)  //0 입력 방지
        {
            binding.editQunt.setText(UtilFuncs().floatFormat(oldQunt))
            return  oldQunt
        }else{  //비율 계산
            qunt = newQunt / oldQunt
        }

        // 재료 텍스트 새로고침
        for (index in 0 until  iList.size - 1 ) {
            iList[index].qunt = iList[index].qunt * qunt
        }
        iAdapter.notifyDataSetChanged()
        return  newQunt
    }

    // 재료 검색
    private fun selectIList(dList: DList) {
        //         firestore 요리 리스트 Query
        iList.clear()
        binding.pbIlist.visibility = View.VISIBLE   //프로그레스바 활성
        db.collection("IList")  //파이어스토어 IList
            .whereEqualTo("id", dList.id)   // 요리명만 검색
            .get().addOnSuccessListener {  result ->
                for (document in result) {
                    var item = IList(
                        document["id"] as String,
                        document.id,
                        "",
                        "${document["qunt"]}".toFloat() ,
                        document["unit"] as String,
                        document["essential"] as Boolean )

                    db.collection("IName")  //IName 재료명 검색
                        .whereEqualTo("SEQ", document.id)   //재료 ID dlfcl
                        .whereEqualTo("code", langCode) // 설정 언어만
                        .get().addOnSuccessListener { result ->
                            for (document in result) {
                                var index = iList.indexOfFirst{
                                    it.SEQ == document["SEQ"] as String }
                                iList[index].name = document["name"] as String
                            }
                        }.addOnFailureListener {
                            Log.d("FireStore","${document["SEQ"]}")
                        }.addOnCompleteListener {
                            iAdapter.notifyDataSetChanged() //검색 데이터 적용
                            binding.pbIlist.visibility = View.INVISIBLE //프로그래스 비활성
                            // 재료 정렬
                        }
                    iList.add(item) //
                }
            }.addOnFailureListener {
                Log.d("FireStore","IList Fail")
            }
    }

    // 레시피 검색
    private fun selectRList(dList: DList) {
        //         firestore 요리 리스트 Query
        rList.clear()
        binding.pbRlist.visibility = View.VISIBLE   //프로그레스 활성화
        db.collection("RList")//.orderBy("seq") //Rlist 레시피 검색
            .whereEqualTo("id", dList.id)       // 요리명만 검색
            .get().addOnSuccessListener {  result ->
                for (document in result) {
                    var item = RList(
                        document["id"] as String,
                        document["seq"] as String,
                        "",
                        "${document["time"]}".toFloat()  )

                    db.collection("RName")  // RName 레시피 검색
                        .whereEqualTo("SEQ", document["seq"] as String) //레시피 ID
                        .whereEqualTo("code", langCode) //설정 언어 검색
                        .get().addOnSuccessListener { result ->
                            for (document in result) {
                                var index = rList.indexOfFirst{
                                    it.SEQ == document["SEQ"] as String }
                                rList[index].name = document["name"] as String
                            }
                        }.addOnFailureListener {
                            Log.d("FireStore","${document["SEQ"]}")
                        }.addOnCompleteListener {
                            rAdapter.notifyDataSetChanged() //데이터 적용
                            binding.pbRlist.visibility = View.INVISIBLE // 프로그레스바 해제
                            //레시피 정렬
                        }
                    rList.add(item)
                }
            }.addOnFailureListener {
                Log.d("FireStore","RList Fail")
            }
    }
    
    //즐겨찾기 버튼 아이콘 설정
    fun setFavoritesButton(savedFavorites: Boolean) {
        if (savedFavorites) {   //즐겨찾기 등록됨
            binding.btnFavorites.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_baseline_star_24,0,0,0)
        } else {    //즐겨찾기 등록 안됨
            binding.btnFavorites.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_baseline_star_border_24,0,0,0)
        }
    }

    // 유튜브 SeeKto
    override fun onRItemSelected(time: Float) {
        mYoutubePlayer?.seekTo(time)
    }
}

// Youtube Listener
class AbYoutubePlayerListener(videoId: String, second: Float): AbstractYouTubePlayerListener() {
    val videoId = videoId   // Youtube Id
    var second = second // 시작시간
    override fun onReady(youTubePlayer: YouTubePlayer){
        if (App.prefs.getBoolean("play",true)){
            youTubePlayer.loadVideo(videoId, second)    //자동 재생
        }else{
            youTubePlayer.cueVideo(videoId, second)     //재생 없이 대기
        }
//        var customUI = YourCustomPlayerUiController(youTubePlayer,youTubePlayerView,customPlayerUi)

//        youTubePlayer.addListener(tracker)
        mYoutubePlayer = youTubePlayer  // 인터페이스 없이 컨트롤 위한 변수
    }
//
//    override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
//        super.onCurrentSecond(youTubePlayer, second)
//    }
}

