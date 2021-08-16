package com.ths.thethskitchen_git_ver2021072601

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenResumed
import androidx.viewpager2.widget.ViewPager2
import com.skydoves.balloon.*
import com.ths.thethskitchen_git_ver2021072601.databinding.ActivityMainBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : BaseActivity() {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater)}
    private var exit = false
    private lateinit var balloon1: Balloon
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setLanguge()    //최초 실행시 언어설정(한국은 자막X)
        //메인화면 뷰 페이져
        val viewPager = ViewPagerAdapter(this  )
        viewPager.addFragment(SearchFragment()) // 검색
        viewPager.addFragment(ListFragment())   // 추천 리스트
        binding.viewPager.adapter = viewPager
        // 메뉴 해더 유튜브로 이동
        val header = binding.navi.getHeaderView(0)
        header.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.youtube.com/channel/UCM_NeHV1e_QZ5dYr-C-TJqA"))
            startActivity(intent)
        }

        // 메뉴 아이템 
        binding.navi.setNavigationItemSelectedListener {
            when(it.itemId){
                binding.navi.menu.getItem(0).itemId -> {    // 검색창으로 이동
                    binding.viewPager.currentItem = 0
                    binding.drawer.closeDrawers()
                }
                binding.navi.menu.getItem(1).itemId -> {    //추천 리스트로 이동
                    binding.viewPager.currentItem = 1
                    binding.drawer.closeDrawers()
                }
                binding.navi.menu.getItem(2).itemId -> {//우리집 부엌
                    val intent = Intent(this,KitchenActivity::class.java)
                    startActivity(intent)
                }
                binding.navi.menu.getItem(3).itemId -> {    //우리집냉장고
                    val intent = Intent(this, RefrigeratorActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                    startActivity(intent)
                }
                binding.navi.menu.getItem(4).itemId -> {    //장바구니
                    val intent = Intent(this,CartActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                    startActivity(intent)
                }
                binding.navi.menu.getItem(5).itemId -> {    //즐겨찾기
                    val intent = Intent(this,FavoritesActivity::class.java)
                    startActivity(intent)
                }
                binding.navi.menu.getItem(6).itemId -> {    //설정
                    val intent = Intent(this, SettingActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                    startActivity(intent)
                }
                binding.navi.menu.getItem(7).itemId -> { //연락하기
                    val email = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:thstudio6158@gmail.com"))
                    startActivity(email)
                }

                binding.navi.menu.getItem(8).itemId -> { //도움말

                }
                else -> Toast.makeText(this,"text",Toast.LENGTH_LONG).show()
            }
            return@setNavigationItemSelectedListener true
        }
        
        // 메뉴버튼
        binding.btnMenu.setOnClickListener{
            binding.drawer.openDrawer(GravityCompat.START)
        }
        binding.btnMove.setOnClickListener {
            when (binding.viewPager.currentItem){
                0 -> binding.viewPager.currentItem = 1
                1 -> binding.viewPager.currentItem = 0
            }
        }

        
        // 페이지 이동시 콜백
        binding.viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when(position){
                    0 -> {
                        binding.imgRecommand.visibility = View.GONE
                        binding.btnLog.visibility = View.VISIBLE
                        binding.txtTitle.text = getString(R.string.app_name)
                        binding.btnMove.setImageResource(R.drawable.ic_baseline_arrow_forward_ios_36)
//                        balloon1.dismiss()
                    }
                    1 -> {
                        binding.imgRecommand.visibility = View.VISIBLE
                        binding.btnLog.visibility = View.GONE
                        binding.txtTitle.text = getString(R.string.menu_recommandList)
                        binding.btnMove.setImageResource(R.drawable.ic_baseline_arrow_back_ios_36)
                        if (App.prefs.getBoolean("help",true)){
                            balloon1 = createBalloon(applicationContext) {
                                setArrowSize(10)
                                setWidth(BalloonSizeSpec.WRAP)
                                setHeight(BalloonSizeSpec.WRAP)
                                setArrowPosition(0.1f)
                                setCornerRadius(4f)
                                setAlpha(0.9f)
                                setPadding(10)
                                setMarginTop(10)
//                                setMarginLeft(150)
                                setTextSize(14.0f)
                                 setAutoDismissDuration(5000L)
                                setText(getString(R.string.h_recommand))
                                setTextColorResource(R.color.white)
                                setTextIsHtml(true)
                                setArrowOrientation(ArrowOrientation.BOTTOM)
                                setBackgroundColorResource(R.color.thscolor)
                                setBalloonAnimation(BalloonAnimation.FADE)
                                setLifecycleOwner(lifecycleOwner) }
                            balloon1.setOnBalloonClickListener {
                                balloon1.dismiss()
                            }
                            binding.btnMenu.showAlignBottom(balloon1)
                        }

                    }
                }
            }
        })
        //도움말
//        val dialog = ActivityHelpBinding.inflate(layoutInflater)
//        val builder = AlertDialog.Builder(this)
//        builder.setView(dialog.root)

//        val alertDialog = builder.create()
//        dialog.btnSkip.setOnClickListener {
//            alertDialog.dismiss()
//            Log.d("HelpScreen","Skip")
//        }
//
//        alertDialog.show()
    }

    // 초기 언어 설정
    private fun setLanguge() {
        var langCode = App.prefs.getString("code","") // 빈값인 경우 최초 실행
        val codeList: List<String> = listOf("ko", "en", "zh-CN", "zh-TW", "es", "ar", "hi",
            "bn", "pt",	"ru", "ja",	"jw", "tr", "fr", "de", "te", "mr", "ur", "vi", "ta", "it",
            "fa", "ms", "gu")   //사용 가능 언어

        if (langCode == "") {
            App.prefs.setBoolena("stove",true)
            App.prefs.setBoolena("oven",true)
            App.prefs.setBoolena("micro",true)
            App.prefs.setBoolena("blender",true)
            App.prefs.setBoolena("multi",true)
            App.prefs.setBoolena("airfryer",true)
            App.prefs.setBoolena("steam",true)
            App.prefs.setBoolena("sous",true)
            App.prefs.setBoolena("grill",true)

            val language = Locale.getDefault().language
            if (langCode == "ko"){  //한국
                App.prefs.setBoolena("caption",false)   //한국인 경우 초기 자막 없음 설정
            }
            when {
                codeList.any { it == language } -> {
                    langCode = language //사용 가능한 언어 있으면 기본 설정
                }
                langCode == "jv" -> { //자바어 데이터 jw로 잘못 입력
                    langCode = "jw" //자바어
                }
                langCode.substring(0,1) == "zh" -> { //중국어인 경우
                    val strLang = Locale.getDefault().toString()
                    langCode = when (strLang.substring(3,4)) {
                        "HK"  -> "zh-TW"    //홍콩
                        "TW" -> "zh-TW"      //대만
                        "MO" -> "zh-TW"      //마카오
                        "Ha" -> "zh-TW"      //번체
                        else -> "zh-CN"     //기타 중국
                    }
                }
                codeList.any {it == language.substring(0,1)} -> {    //기타 언어 중 사용가능언어의 세부 분류
                    langCode = language.substring(0,1)  // ex pt-pt -> pt
                }
                else -> {  // 기타 언어 영어
                    langCode ="en"
                }
            }
            App.prefs.setString("code",langCode)    //preferenc 언어세팅 저장
        }


    }

    override fun onBackPressed() {
        //드로워 메뉴 백버튼
        if (binding.drawer.isDrawerOpen(GravityCompat.START)) {
            binding.drawer.closeDrawers()
        }else if(binding.viewPager.currentItem == 1) {
            binding.viewPager.currentItem = 0
        }else if(binding.viewPager.currentItem == 0) {
            if (exit){
                super.onBackPressed()
            }else{
                Toast.makeText(this,getString(R.string.msg_exit),Toast.LENGTH_SHORT).show()
                exit = true
                lifecycleScope.launch {
                    whenResumed {
                        while (exit) {
                            delay(2000)
                            exit = false
                        }
                    }
                }
            }
        }else{  //드로워 아닌경우
            super.onBackPressed()
        }
    }
}

