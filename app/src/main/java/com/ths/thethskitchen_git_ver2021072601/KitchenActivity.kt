package com.ths.thethskitchen_git_ver2021072601

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenResumed
import com.ths.thethskitchen_git_ver2021072601.databinding.ActivityKitchenBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

//나의 부엌 설정
class KitchenActivity : AppCompatActivity() {
    val binding by lazy { ActivityKitchenBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //체크버튼 세팅
        binding.chkStove.isChecked = App.prefs.getBoolean("stove",false)
        binding.chkOven.isChecked = App.prefs.getBoolean("oven",false)
        binding.chkMicro.isChecked = App.prefs.getBoolean("micro",false)
        binding.chkBlender.isChecked = App.prefs.getBoolean("blender",false)
        binding.chkMulti.isChecked = App.prefs.getBoolean("multi",false)
        binding.chkAirfryer.isChecked = App.prefs.getBoolean("airfryer",false)
        binding.chkSteam.isChecked = App.prefs.getBoolean("steam",false)
        binding.chkSous.isChecked = App.prefs.getBoolean("sous",false)
        binding.chkGrill.isChecked = App.prefs.getBoolean("grill",false)

        //상단뷰
        val viewPager = ViewPagerAdapter(this)
        viewPager.addFragment(P1Fragment()) //이미지1
        viewPager.addFragment(P2Fragment()) //이미지2
        viewPager.addFragment(P3Fragment()) //이미지3
        viewPager.addFragment(P4Fragment()) //이미지4
        viewPager.addFragment(P5Fragment()) //이미지5
        binding.vpKitchen.adapter = viewPager   //상단뷰 페이져
        
        binding.chkStove.setOnCheckedChangeListener { buttonView, isChecked ->
            App.prefs.setBoolena("stove",isChecked)
        }   //레인지
        binding.chkOven.setOnCheckedChangeListener { buttonView, isChecked ->
            App.prefs.setBoolena("oven",isChecked)
        }   //오븐
        binding.chkMicro.setOnCheckedChangeListener { buttonView, isChecked ->
            App.prefs.setBoolena("micro",isChecked)
        }   //전자레인지
        binding.chkBlender.setOnCheckedChangeListener { buttonView, isChecked ->
            App.prefs.setBoolena("blender",isChecked)
        }   //블렌더
        binding.chkAirfryer.setOnCheckedChangeListener { buttonView, isChecked ->
            App.prefs.setBoolena("airfryer",isChecked)
        }   //에어프라이어
        binding.chkMulti.setOnCheckedChangeListener { buttonView, isChecked ->
            App.prefs.setBoolena("multi",isChecked)
        }   //인스턴트팟
        binding.chkSteam.setOnCheckedChangeListener { buttonView, isChecked ->
            App.prefs.setBoolena("steam",isChecked)
        }   //찜기
        binding.chkSous.setOnCheckedChangeListener { buttonView, isChecked ->
            App.prefs.setBoolena("sous",isChecked)
        }   //수비드
        binding.chkGrill.setOnCheckedChangeListener { buttonView, isChecked ->
            App.prefs.setBoolena("grill",isChecked)
        }   //그릴

        //종료
        binding.btnKExit.setOnClickListener {
            finish()
        }
        
        //이미지 로딩을 위한 스레드
        var isRunning = true
        lifecycleScope.launch {
            whenResumed {
                while (isRunning) {
                    delay(3000)
                    binding.vpKitchen.currentItem?.let {
                        binding.vpKitchen.setCurrentItem(it.plus(1) % 5 )
                    }
                }
            }
        }
    }
}