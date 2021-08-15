package com.ths.thethskitchen_git_ver2021072601

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenResumed
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.firestore.FirebaseFirestore
import com.skydoves.balloon.*
import com.ths.thethskitchen_git_ver2021072601.databinding.ActivityKitchenBinding
import kotlinx.coroutines.*

//나의 부엌 설정
class KitchenActivity : BaseActivity() {
    val binding by lazy { ActivityKitchenBinding.inflate(layoutInflater) }
    var list = arrayListOf<String>()
    var adapter = KitchenAdapter(list)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.viewReview.adapter = adapter
        binding.viewReview.orientation = ViewPager2.ORIENTATION_HORIZONTAL


        //체크버튼 세팅
        binding.chkStove.isChecked = App.prefs.getBoolean("stove",true)
        binding.chkOven.isChecked = App.prefs.getBoolean("oven",true)
        binding.chkMicro.isChecked = App.prefs.getBoolean("micro",true)
        binding.chkBlender.isChecked = App.prefs.getBoolean("blender",true)
        binding.chkMulti.isChecked = App.prefs.getBoolean("multi",true)
        binding.chkAirfryer.isChecked = App.prefs.getBoolean("airfryer",true)
        binding.chkSteam.isChecked = App.prefs.getBoolean("steam",true)
        binding.chkSous.isChecked = App.prefs.getBoolean("sous",true)
        binding.chkGrill.isChecked = App.prefs.getBoolean("grill",true)

        CoroutineScope(Dispatchers.IO).launch {
            val db = FirebaseFirestore.getInstance()
            db.collection("URList").get().addOnSuccessListener { result ->
                for (document in result) {
                    val url = document["URL"] as String
                    list.add(url)
                }
//                withContext(Dispatchers.Main){
//                    Log.d("ListAdapter","${list.size}")
//                    Log.d("ListAdapter","Dispatchers")
                    adapter.notifyDataSetChanged()
//                }
            }
        }

        
        binding.chkStove.setOnCheckedChangeListener { _, isChecked ->
            App.prefs.setBoolena("stove",isChecked)
        }   //레인지
        binding.chkOven.setOnCheckedChangeListener { _, isChecked ->
            App.prefs.setBoolena("oven",isChecked)
        }   //오븐
        binding.chkMicro.setOnCheckedChangeListener { _, isChecked ->
            App.prefs.setBoolena("micro",isChecked)
        }   //전자레인지
        binding.chkBlender.setOnCheckedChangeListener { _, isChecked ->
            App.prefs.setBoolena("blender",isChecked)
        }   //블렌더
        binding.chkAirfryer.setOnCheckedChangeListener { _, isChecked ->
            App.prefs.setBoolena("airfryer",isChecked)
        }   //에어프라이어
        binding.chkMulti.setOnCheckedChangeListener { _, isChecked ->
            App.prefs.setBoolena("multi",isChecked)
        }   //인스턴트팟
        binding.chkSteam.setOnCheckedChangeListener { _, isChecked ->
            App.prefs.setBoolena("steam",isChecked)
        }   //찜기
        binding.chkSous.setOnCheckedChangeListener { _, isChecked ->
            App.prefs.setBoolena("sous",isChecked)
        }   //수비드
        binding.chkGrill.setOnCheckedChangeListener { _, isChecked ->
            App.prefs.setBoolena("grill",isChecked)
        }   //그릴

        //종료
        binding.btnKExit.setOnClickListener {
            finish()
        }


//        //이미지 로딩을 위한 스레드
        val isRunning = true
        lifecycleScope.launch {
            whenResumed {
                while (isRunning) {
                    delay(3000)
                    if (list.size > 0){
                        binding.viewReview.currentItem.let {
                            binding.viewReview.currentItem = it.plus(1)
                        }
                    }
                }
            }
        }
        if ( App.prefs.getBoolean("stove",true) &&
            App.prefs.getBoolean("oven",true)&&
            App.prefs.getBoolean("micro",true) &&
            App.prefs.getBoolean("blender",true) &&
            App.prefs.getBoolean("multi",true) &&
            App.prefs.getBoolean("airfryer",true) &&
            App.prefs.getBoolean("steam",true) &&
            App.prefs.getBoolean("sous",true) &&
            App.prefs.getBoolean("grill",true) &&
            !SQLiteDBHelper(this,"THsKitchen.db",1).existsRefiregierator()){
            createHelp()
        }
    }

    private fun createHelp() {
        val balloon = createBalloon(this) {
            setArrowSize(10)
            setWidth(BalloonSizeSpec.WRAP)
            setHeight(BalloonSizeSpec.WRAP)
            setArrowPosition(0.1f)
            setCornerRadius(4f)
            setAlpha(0.9f)
            setPadding(10)
            setMarginTop(15)
            setMarginLeft(8)
            setTextSize(14.0f)
            setAutoDismissDuration(5000L)
            setText(getString(R.string.h_k_empty1))
            setTextColorResource(R.color.white)
            setTextIsHtml(true)
            arrowOrientation = ArrowOrientation.BOTTOM
            setBackgroundColorResource(R.color.thscolor)
            setBalloonAnimation(BalloonAnimation.FADE)
            setLifecycleOwner(lifecycleOwner)
        }
        balloon.setOnBalloonClickListener {
            balloon.dismiss()
        }

        val balloon2 = createBalloon(this) {
            setArrowSize(10)
            setWidth(BalloonSizeSpec.WRAP)
            setHeight(BalloonSizeSpec.WRAP)
            setArrowPosition(0.1f)
            setCornerRadius(4f)
            setAlpha(0.9f)
            setPadding(10)
            setMarginBottom(15)
//            setMarginLeft(8)
            setTextSize(14.0f)
            balloonHighlightAnimationStartDelay = 2000L
            setAutoDismissDuration(6000L)
            setText(getString(R.string.h_k_empty2))
            setTextColorResource(R.color.white)
            setTextIsHtml(true)
            arrowOrientation = ArrowOrientation.BOTTOM
            setBackgroundColorResource(R.color.thscolor)
            setBalloonAnimation(BalloonAnimation.FADE)
            setLifecycleOwner(lifecycleOwner)
        }
        balloon2.setOnBalloonClickListener {
            balloon2.dismiss()
        }
        binding.btnKExit.showAlignBottom(balloon)
        binding.tableLayout.showAlignBottom(balloon2)
    }
}