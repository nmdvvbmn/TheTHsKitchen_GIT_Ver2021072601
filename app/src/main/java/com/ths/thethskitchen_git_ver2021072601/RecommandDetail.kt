package com.ths.thethskitchen_git_ver2021072601

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.skydoves.balloon.*
import com.ths.thethskitchen_git_ver2021072601.databinding.ActivityRecommandDetailBinding
import java.lang.Exception

var mYoutubePlayer: YouTubePlayer? = null

@Suppress("NAME_SHADOWING")
class RecommandDetail : BaseActivity(), RListAdapter.OnRItem {
    val binding by lazy { ActivityRecommandDetailBinding.inflate(layoutInflater) }
    val helper =  SQLiteDBHelper(this,App.dbName, App.dbVer)   // 장바구니, 즐겨찾기 로컬 DB
    private val iList = arrayListOf<IList>()    //재료 리스트
    private var iAdapter = IListAdapter(iList)  //재료 어뎁터
    private val rList = arrayListOf<RList>()    //레시피
    private val rAdapter = RListAdapter(rList,this) //레시피 어뎁터
    private var oldQunt : Float = 0F    // 재료량 계산을 위한 이전값 저장
    private val langCode = UtilFuncs().getLanguage() // 저장된 언어
    private lateinit var balloon1: Balloon
    private lateinit var balloon2: Balloon
    private lateinit var balloon3: Balloon
    private lateinit var balloon4: Balloon

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val dlist = intent.getSerializableExtra("data") as DList    // 이전 페이지 선택 데이터
        var caption = 1 // 캡션설정 (디폴트 자막있음)
        //자막 없음 설정인 경우
        if (!App.prefs.getBoolean("caption",true)) {
            caption = 0
        }

        val option = IFramePlayerOptions.Builder().ccLoadPolicy(caption).build()    //자막설정을 위한 옵션
        val youtubePlayerListener = AbYoutubePlayerListener(dlist.video, dlist.start.toFloat())
        // 유튜브API 리스너

        lifecycle.addObserver(binding.youtubeView)
        binding.youtubeView.enableAutomaticInitialization = false   // 옵션 선택시 자동초기화 false
        binding.youtubeView.initialize(youtubePlayerListener,true,option)   //자막을 위한 수동 초기화

        var savedFavorites = helper.existsFavorites(dlist.video) //즐겨찾기 저장 유무
        setFavoritesButton(savedFavorites)  // 즐겨찾기 버튼

        oldQunt = dlist.qunt.toFloat()  // 기본 양 
        binding.editQunt.setText(UtilFuncs().floatFormat(oldQunt))  //Float->Int(String)
        binding.txtUnit.setText(UtilFuncs().transUnit(dlist.quntunit))  //단위 언어에 맞게 세팅

        if (dlist.id == "") {
            binding.txtTool.visibility = View.GONE
            binding.editQunt.visibility = View.GONE
            binding.txtUnit.visibility = View.GONE
            binding.recycleIList.visibility = View.GONE
            binding.recycleRList.visibility = View.GONE
            binding.pbIlist.visibility = View.GONE
            binding.pbRlist.visibility = View.GONE
            binding.txtRDesc.text = dlist.desc
            binding.txtItext.visibility = View.GONE
            binding.txtRtext.visibility = View.GONE

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
            binding.txtPtime.visibility = View.GONE
            binding.txtPTEXT.visibility = View.GONE
            binding.txtPunit.visibility = View.GONE
        }else{
            binding.txtPtime.text = dlist.pretime.toString()
            binding.txtPunit.text = getString(UtilFuncs().transUnit((dlist.preunit)))//준비시간 + 단위
        }

        if (dlist.time <= 0) {
            binding.txtCtime.visibility = View.GONE
            binding.txtCutin.visibility = View.GONE
            binding.txtCTEXT.visibility = View.GONE
        }else{
            binding.txtCtime.text = dlist.time.toString()
            binding.txtCutin.text = getString(UtilFuncs().transUnit((dlist.timeunit)))//준비시간 + 단위
        }




        //즐겨찾기 버튼
        binding.btnFavorites.setOnClickListener {
            savedFavorites = if (savedFavorites) {   //즐겨찾기 취소
                helper.deleteFavorites(dlist)  //favoriate에서 삭제
                binding.btnFavorites.setImageResource(R.drawable.ic_baseline_star_border_24)
                false //저장 안됨 플래그
            }else{  //즐겨찾기 등록
                helper.insertFavorites(dlist)  //favoriate insert
                binding.btnFavorites.setImageResource(R.drawable.ic_baseline_star_24)
                true   //저장됨 플랙그
            }
        }

        // 공유버튼
        binding.btnShare.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            val text = dlist.name + "\n" +
                    "https://www.youtube.com/watch?v=" + dlist.video //유튜브 URL만 전달(임시)
            intent.putExtra(Intent.EXTRA_TEXT,text)
            val chooser = Intent.createChooser(intent, this.getString(R.string.title_share))
            startActivity(chooser)
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
            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            startActivity(intent)
//            iAdapter.notifyDataSetChanged()
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
        binding.txtRDesc.movementMethod = ScrollingMovementMethod()

        //도움말
        if (dlist.id != "" && App.prefs.getBoolean("help",true)){
            createHelp()
        }
    }

    private fun createHelp() {
        balloon1 = createBalloon(this) {
            setArrowSize(10)
            setWidth(BalloonSizeSpec.WRAP)
            setHeight(BalloonSizeSpec.WRAP)
            setArrowPosition(0.1f)
            setCornerRadius(4f)
            setAlpha(0.9f)
            setPadding(10)
            setMarginBottom(10)
            setMarginLeft(150)
            setTextSize(14.0f)
            setAutoDismissDuration(5000L)
            setText(getString(R.string.h_qunt_adj))
            setTextColorResource(R.color.white)
            setTextIsHtml(true)
            setArrowOrientation(ArrowOrientation.BOTTOM)
            setBackgroundColorResource(R.color.thscolor)
            setBalloonAnimation(BalloonAnimation.FADE)
            setLifecycleOwner(lifecycleOwner) }


        balloon2 = createBalloon(this) {
            setArrowSize(10)
            setWidth(BalloonSizeSpec.WRAP)
            setHeight(BalloonSizeSpec.WRAP)
            setArrowPosition(0.8f)
            setCornerRadius(4f)
            setAlpha(0.9f)
            setPadding(10)
            setMarginTop(50)
            setMarginLeft(50)
            setTextSize(14.0f)
//            setAutoDismissDuration(5000L)
            setText(getString(R.string.h_cart))
            setTextColorResource(R.color.white)
            setTextIsHtml(true)
            setArrowOrientation(ArrowOrientation.TOP)
            setBackgroundColorResource(R.color.thscolor)
            setBalloonAnimation(BalloonAnimation.FADE)
            setLifecycleOwner(lifecycleOwner) }


        balloon3 = createBalloon(this) {
            setArrowSize(10)
            setWidth(BalloonSizeSpec.WRAP)
            setHeight(BalloonSizeSpec.WRAP)
            setArrowPosition(0.8f)
            setCornerRadius(4f)
            setAlpha(0.9f)
            setPadding(10)
            setMarginTop(100)
            setMarginLeft(50)
            setTextSize(14.0f)
//            setAutoDismissDuration(5000L)
            setText(getString(R.string.h_time))
            setTextColorResource(R.color.white)
            setTextIsHtml(true)
            setArrowOrientation(ArrowOrientation.TOP)
            setBackgroundColorResource(R.color.thscolor)
            setBalloonAnimation(BalloonAnimation.FADE)
            setLifecycleOwner(lifecycleOwner) }

        balloon4 = createBalloon(this) {
            setArrowSize(10)
            setWidth(BalloonSizeSpec.WRAP)
            setHeight(BalloonSizeSpec.WRAP)
            setArrowPosition(0.9f)
            setCornerRadius(4f)
            setAlpha(0.9f)
            setPadding(10)
            setMarginTop(10)
//            setMarginLeft(50)
            setTextSize(14.0f)
//            setAutoDismissDuration(5000L)
            setText(getString(R.string.h_movecart))
            setTextColorResource(R.color.white)
            setTextIsHtml(true)
            setArrowOrientation(ArrowOrientation.TOP)
            setBackgroundColorResource(R.color.thscolor)
            setBalloonAnimation(BalloonAnimation.FADE)
            setLifecycleOwner(lifecycleOwner) }
        binding.txtUnit.showAlignTop(balloon1)
        binding.txtItext.showAlignBottom(balloon2)
        binding.txtRtext.showAlignBottom(balloon3)
        binding.btnToCart.showAlignBottom(balloon4)
        balloon1.setOnBalloonDismissListener {
            balloon2.dismissWithDelay(300L)
        }
        balloon2.setOnBalloonDismissListener {
            balloon3.dismissWithDelay(300L)
        }
        balloon3.setOnBalloonDismissListener {
            balloon4.dismissWithDelay(300L)
        }
        balloon4.setOnBalloonDismissListener {
            balloon1.dismissWithDelay(300L)
        }
        balloon1.setOnBalloonClickListener {
            balloon1.dismiss()
        }

        balloon2.setOnBalloonClickListener {
            balloon2.dismiss()
        }

        balloon3.setOnBalloonClickListener {
            balloon3.dismiss()
        }

        balloon4.setOnBalloonClickListener {
            balloon4.dismiss()
        }

    }

    //조리도구 텍스트
    private fun setTool(dlist: DList) {
        var toolTxt: String
        if (dlist.stove > 0 || dlist.oven > 0 || dlist.micro > 0 || dlist.blender > 0 ||
            dlist.airfryer > 0 || dlist.multi > 0 || dlist.steamer > 0 || dlist.sousvide > 0 ||
            dlist.grill > 0 ) {
            toolTxt = this.getString(R.string.tool) + "\n"
            if (dlist.stove > 0) {  //레인지
                toolTxt += this.getString(R.string.stove)
                toolTxt = if (dlist.stove < 2) toolTxt + this.getString(R.string.option) + "\n"
                else toolTxt + "\n"
            }
            if (dlist.oven > 0) {   //오븐
                toolTxt += this.getString(R.string.oven)
                toolTxt = if (dlist.oven < 2) toolTxt + this.getString(R.string.option) + "\n"
                else toolTxt + "\n"
            }
            if (dlist.micro > 0) {  //전자레인지
                toolTxt += this.getString(R.string.micro)
                toolTxt = if (dlist.micro < 2) toolTxt + this.getString(R.string.option) + "\n"
                else toolTxt + "\n"
            }
            if (dlist.blender > 0) {    //블랜더
                toolTxt += this.getString(R.string.blender)
                toolTxt = if (dlist.blender < 2) toolTxt + this.getString(R.string.option) + "\n"
                else toolTxt + "\n"
            }
            if (dlist.airfryer > 0) {   //에어프라이어
                toolTxt += this.getString(R.string.airfryer)
                toolTxt = if (dlist.airfryer < 2) toolTxt + this.getString(R.string.option) + "\n"
                else toolTxt + "\n"
            }
            if (dlist.multi > 0) {  //인스턴트팟
                toolTxt += this.getString(R.string.multi)
                toolTxt = if (dlist.multi < 2) toolTxt + this.getString(R.string.option) + "\n"
                else toolTxt + "\n"
            }
            if (dlist.steamer > 0) {    //찜기
                toolTxt += this.getString(R.string.steam)
                toolTxt = if (dlist.steamer < 2) toolTxt + this.getString(R.string.option) + "\n"
                else toolTxt + "\n"
            }
            if (dlist.sousvide > 0) {       //수비드
                toolTxt += this.getString(R.string.sous)
                toolTxt = if (dlist.sousvide < 2) toolTxt + this.getString(R.string.option) + "\n"
                else toolTxt + "\n"
            }
            if (dlist.grill > 0) {  //그릴
                toolTxt += this.getString(R.string.grill)
                toolTxt = if (dlist.grill < 2) toolTxt + this.getString(R.string.option) + "\n"
                else toolTxt + "\n"
            }
            binding.txtTool.text = toolTxt
        }else{  //조리도구 없는경우
            binding.txtTool.visibility = View.INVISIBLE
        }
    }

    // 조리량에 따른 재료 량 변경
    private fun setItemQunt(oldQunt: Float, newQunt: Float ) : Float {
        val qunt: Float   //변경 비율
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
        App.db.collection("IList")  //파이어스토어 IList
            .whereEqualTo("id", dList.id)   // 요리명만 검색
            .get().addOnSuccessListener {  result ->
                for (document in result) {
                     val item = IList(
                        document["id"] as String,
                        document.id,
                        "",
                        "${document["qunt"]}".toFloat() ,
                        document["unit"] as String,
                        document["essential"] as Boolean )

                    App.db.collection("IName")  //IName 재료명 검색
                        .whereEqualTo("SEQ", document.id)   //재료 ID dlfcl
                        .whereEqualTo("code", langCode) // 설정 언어만
                        .get().addOnSuccessListener { result ->
                            for (document in result) {
                                val index = iList.indexOfFirst{
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
        App.db.collection("RList")//.orderBy("seq") //Rlist 레시피 검색
            .whereEqualTo("id", dList.id)       // 요리명만 검색
            .get().addOnSuccessListener {  result ->
                for (document in result) {
                    val item = RList(
                        document["id"] as String,
                        document["seq"] as String,
                        "",
                        "${document["time"]}".toFloat()  )

                    App.db.collection("RName")  // RName 레시피 검색
                        .whereEqualTo("SEQ", document["seq"] as String) //레시피 ID
                        .whereEqualTo("code", langCode) //설정 언어 검색
                        .get().addOnSuccessListener { result ->
                            for (document in result) {
                                val index = rList.indexOfFirst{
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
    private fun setFavoritesButton(savedFavorites: Boolean) {
        if (savedFavorites) {   //즐겨찾기 등록됨
            binding.btnFavorites.setImageResource(R.drawable.ic_baseline_star_24)
        } else {    //즐겨찾기 등록 안됨
            binding.btnFavorites.setImageResource(R.drawable.ic_baseline_star_border_24)
        }
    }

    // 유튜브 SeeKto
    override fun onRItemSelected(time: Float) {
        mYoutubePlayer?.play()
        mYoutubePlayer?.seekTo(time)
    }

    override fun onResume() {
        super.onResume()
        iAdapter.notifyDataSetChanged()
    }

    override fun onBackPressed() {
        try{
            balloon1.dismiss()
            balloon2.dismiss()
            balloon3.dismiss()
            balloon4.dismiss()
        }catch (e: Exception){

        }finally {
            super.onBackPressed()
        }
    }

}

// Youtube Listener
class AbYoutubePlayerListener(private val videoId: String, var second: Float): AbstractYouTubePlayerListener() {
    override fun onReady(youTubePlayer: YouTubePlayer){
        if (App.prefs.getBoolean("play",true)){
            youTubePlayer.loadVideo(videoId, second)    //자동 재생
        }else{
            youTubePlayer.loadVideo(videoId, second)    //자동 재생
            youTubePlayer.pause()
//            youTubePlayer.cueVideo(videoId, second)     //재생 없이 대기
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

