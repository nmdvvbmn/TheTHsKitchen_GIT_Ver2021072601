package com.ths.thethskitchen_git_ver2021072601

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenResumed
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.skydoves.balloon.*
import com.ths.thethskitchen_git_ver2021072601.databinding.ActivityKitchenBinding
import kotlinx.coroutines.*
import java.lang.Exception

//나의 부엌 설정
class KitchenActivity : BaseActivity() {
    val binding by lazy { ActivityKitchenBinding.inflate(layoutInflater) }
    var list = arrayListOf<String>()
    var adapter = KitchenAdapter(list)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //광고
        MobileAds.initialize(this)
        val adRequest = AdRequest.Builder().build()
        binding.adViewK.loadAd(adRequest)
        binding.adViewK.adListener = object: AdListener() {
            override fun onAdClicked() {
                App.ad = false
                super.onAdClicked()
            }
        }

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
            App.db.collection("URList").get().addOnSuccessListener { result ->
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
            App.changed = true
        }   //레인지
        binding.chkOven.setOnCheckedChangeListener { _, isChecked ->
            App.prefs.setBoolena("oven",isChecked)
            App.changed = true
        }   //오븐
        binding.chkMicro.setOnCheckedChangeListener { _, isChecked ->
            App.prefs.setBoolena("micro",isChecked)
            App.changed = true
        }   //전자레인지
        binding.chkBlender.setOnCheckedChangeListener { _, isChecked ->
            App.prefs.setBoolena("blender",isChecked)
            App.changed = true
        }   //블렌더
        binding.chkAirfryer.setOnCheckedChangeListener { _, isChecked ->
            App.prefs.setBoolena("airfryer",isChecked)
            App.changed = true
        }   //에어프라이어
        binding.chkMulti.setOnCheckedChangeListener { _, isChecked ->
            App.prefs.setBoolena("multi",isChecked)
            App.changed = true
        }   //인스턴트팟
        binding.chkSteam.setOnCheckedChangeListener { _, isChecked ->
            App.prefs.setBoolena("steam",isChecked)
            App.changed = true
        }   //찜기
        binding.chkSous.setOnCheckedChangeListener { _, isChecked ->
            App.prefs.setBoolena("sous",isChecked)
            App.changed = true
        }   //수비드
        binding.chkGrill.setOnCheckedChangeListener { _, isChecked ->
            App.prefs.setBoolena("grill",isChecked)
            App.changed = true
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
        if ( App.prefs.getBoolean("help",true)){
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
        binding.btnKExit.showAlignBottom(balloon)
        binding.tableLayout.showAlignBottom(balloon2)

        balloon.setOnBalloonClickListener {
            try {
                balloon.dismiss()
                balloon2.dismiss()
            }catch (e: Exception){}

        }
        balloon2.setOnBalloonClickListener {
            try {
                balloon.dismiss()
                balloon2.dismiss()
            }catch (e: Exception){}
        }
        balloon.setOnBalloonDismissListener {
            try {
                balloon2.dismissWithDelay(200L)
            }catch (e: Exception){

            }
        }
        balloon2.setOnBalloonDismissListener {
            try {
                balloon.dismissWithDelay(200L)
            }catch (e: Exception){

            }
        }

    }
}