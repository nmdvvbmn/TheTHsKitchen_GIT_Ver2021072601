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
        val viewPager = ViewPagerAdapter(this  )
        viewPager.addFragment(SearchFragment())
        viewPager.addFragment(ListFragment())
        setLanguge()


        binding.viewPager.adapter = viewPager
        var header = binding.navi.getHeaderView(0)
        header.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.youtube.com/channel/UCM_NeHV1e_QZ5dYr-C-TJqA"))
            startActivity(intent)

        }

        binding.navi.setNavigationItemSelectedListener {
            when(it.itemId){
                binding.navi.menu.getItem(0).itemId -> {
                    binding.viewPager.setCurrentItem(0)
                    binding.drawer.closeDrawers()
                }
                binding.navi.menu.getItem(1).itemId -> {
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

                binding.navi.menu.getItem(7).itemId -> Toast.makeText(this,"${it.itemId}",Toast.LENGTH_LONG).show() //도움말
                else -> Toast.makeText(this,"text",Toast.LENGTH_LONG).show()
            }
            return@setNavigationItemSelectedListener true
        }
        binding.btnMenu.setOnClickListener{
            binding.drawer.openDrawer(GravityCompat.START)

        }

        binding.viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
            }
        })

    }

    private fun setLanguge() {
        var langCode = App.prefs.getString("code","")
        val codeList: List<String> = listOf("ko", "en", "zh-CN", "zh-TW", "es", "ar", "hi",
            "bn", "pt",	"ru", "ja",	"jw", "tr", "fr", "de", "te", "mr", "ur", "vi", "ta", "it",
            "fa", "ms", "gu")

        if (langCode == "") {
            var language = Locale.getDefault().getLanguage()
            if (langCode == "ko"){
                App.prefs.setBoolena("caption",false)
            }
            if (language != null) {
                if (codeList.any { it == language } ) {
                    langCode = language
                }else if(langCode == "jv"){
                    langCode = "jw"
                }else if(langCode.substring(0,1) == "zh"){
                    val strLang = Locale.getDefault().toString()
                    when (strLang.substring(3,4)) {
                         "HK"  -> langCode = "zh-TW"
                        "TW" -> langCode = "zh-TW"
                        "MO" -> langCode = "zh-TW"
                        "Ha" -> langCode = "zh-TW"
                        else ->  langCode = "zh-CN"
                    }

                }else if(codeList.any {it == language.substring(0,1)}) {
                    langCode = language.substring(0,1)
                }else{
                    langCode ="en"
                }
            }else{
                langCode ="en"
            }
            App.prefs.setString("code",langCode)
        }
    }

    override fun onBackPressed() {
        //드로워 메뉴 백버튼
        if (binding.drawer.isDrawerOpen(GravityCompat.START)){
            binding.drawer.closeDrawers()
        }else{
            super.onBackPressed()
        }
    }


}

