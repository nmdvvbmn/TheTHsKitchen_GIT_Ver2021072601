package com.ths.thethskitchen_git_ver2021072601

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.viewpager2.widget.ViewPager2
import com.ths.thethskitchen_git_ver2021072601.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater)}

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
        var header = binding.navi.getHeaderView(0)
        header.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.youtube.com/channel/UCM_NeHV1e_QZ5dYr-C-TJqA"))
            startActivity(intent)

        }

        // 메뉴 아이템 
        binding.navi.setNavigationItemSelectedListener {
            when(it.itemId){
                binding.navi.menu.getItem(0).itemId -> {    // 검색창으로 이동
                    binding.viewPager.setCurrentItem(0)
                    binding.drawer.closeDrawers()
                }
                binding.navi.menu.getItem(1).itemId -> {    //추천 리스트로 이동
                    binding.viewPager.setCurrentItem(1)
                    binding.drawer.closeDrawers()
                }
                binding.navi.menu.getItem(2).itemId -> {//우리집 부엌
                    val intent = Intent(this,KitchenActivity::class.java)
                    startActivity(intent)
                }
                binding.navi.menu.getItem(3).itemId -> {    //우리집냉장고
                    val intent = Intent(this, RefrigeratorActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                    startActivity(intent)
                }
                binding.navi.menu.getItem(4).itemId -> {    //장바구니
                    val intent = Intent(this,CartActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                    startActivity(intent)
                }
                binding.navi.menu.getItem(5).itemId -> {    //즐겨찾기
                    val intent = Intent(this,FavoritesActivity::class.java)
                    startActivity(intent)
                }
                binding.navi.menu.getItem(6).itemId -> {    //설정
                    val intent = Intent(this, SettingActivity::class.java)
                    startActivity(intent)
                }
                binding.navi.menu.getItem(7).itemId -> { //연락하기
                    var email = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:thstudio6158@gmail.com"))
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
        
        // 페이지 이동시 콜백
        binding.viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
            }
        })

    }

    // 초기 언어 설정
    private fun setLanguge() {
        var langCode = App.prefs.getString("code","") // 빈값인 경우 최초 실행
        val codeList: List<String> = listOf("ko", "en", "zh-CN", "zh-TW", "es", "ar", "hi",
            "bn", "pt",	"ru", "ja",	"jw", "tr", "fr", "de", "te", "mr", "ur", "vi", "ta", "it",
            "fa", "ms", "gu")   //사용 가능 언어

        if (langCode == "") {
            var language = Locale.getDefault().getLanguage()
            if (langCode == "ko"){  //한국
                App.prefs.setBoolena("caption",false)   //한국인 경우 초기 자막 없음 설정
            }
            if (language != null) {    // 안드로이드 로컬 설정 있는 경우
                if (codeList.any { it == language } ) {
                    langCode = language //사용 가능한 언어 있으면 기본 설정
                }else if(langCode == "jv"){ //자바어 데이터 jw로 잘못 입력
                    langCode = "jw" //자바어
                }else if(langCode.substring(0,1) == "zh"){ //중국어인 경우
                    val strLang = Locale.getDefault().toString()
                    when (strLang.substring(3,4)) {
                         "HK"  -> langCode = "zh-TW"    //홍콩
                        "TW" -> langCode = "zh-TW"      //대만
                        "MO" -> langCode = "zh-TW"      //마카오
                        "Ha" -> langCode = "zh-TW"      //번체
                        else ->  langCode = "zh-CN"     //기타 중국
                    }
                }else if(codeList.any {it == language.substring(0,1)}) {    //기타 언어 중 사용가능언어의 세부 분류
                    langCode = language.substring(0,1)  // ex pt-pt -> pt
                }else{  // 기타 언어 영어
                    langCode ="en"
                }
            }else{  // 안드로이드 로컬 세팅 없는 경우 영어
                langCode ="en"
            }
            App.prefs.setString("code",langCode)    //preferenc 언어세팅 저장
        }
    }

    override fun onBackPressed() {
        //드로워 메뉴 백버튼
        if (binding.drawer.isDrawerOpen(GravityCompat.START)){
            binding.drawer.closeDrawers()
        }else{  //드로워 아닌경우
            super.onBackPressed()
        }
    }
}

