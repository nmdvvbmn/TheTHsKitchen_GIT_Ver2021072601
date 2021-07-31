package com.ths.thethskitchen_git_ver2021072601

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenResumed
import com.ths.thethskitchen_git_ver2021072601.databinding.ActivityKitchenBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
        viewPager.addFragment(P1Fragment())
        viewPager.addFragment(P2Fragment())
        viewPager.addFragment(P3Fragment())
        viewPager.addFragment(P4Fragment())
        viewPager.addFragment(P5Fragment())
        binding.vpKitchen.adapter = viewPager

        binding.chkStove.setOnCheckedChangeListener { buttonView, isChecked ->
            App.prefs.setBoolena("stove",isChecked)
        }
        binding.chkOven.setOnCheckedChangeListener { buttonView, isChecked ->
            App.prefs.setBoolena("oven",isChecked)
        }
        binding.chkMicro.setOnCheckedChangeListener { buttonView, isChecked ->
            App.prefs.setBoolena("micro",isChecked)
        }
        binding.chkBlender.setOnCheckedChangeListener { buttonView, isChecked ->
            App.prefs.setBoolena("blender",isChecked)
        }
        binding.chkAirfryer.setOnCheckedChangeListener { buttonView, isChecked ->
            App.prefs.setBoolena("airfryer",isChecked)
        }
        binding.chkMulti.setOnCheckedChangeListener { buttonView, isChecked ->
            App.prefs.setBoolena("multi",isChecked)
        }
        binding.chkSteam.setOnCheckedChangeListener { buttonView, isChecked ->
            App.prefs.setBoolena("steam",isChecked)
        }
        binding.chkSous.setOnCheckedChangeListener { buttonView, isChecked ->
            App.prefs.setBoolena("sous",isChecked)
        }
        binding.chkGrill.setOnCheckedChangeListener { buttonView, isChecked ->
            App.prefs.setBoolena("grill",isChecked)
        }


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