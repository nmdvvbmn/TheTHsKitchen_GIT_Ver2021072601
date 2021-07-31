package com.ths.thethskitchen_git_ver2021072601

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        App.prefs.getBoolean("stove",binding.chkStove.isChecked)
        App.prefs.getBoolean("oven",binding.chkOven.isChecked)
        App.prefs.getBoolean("micro",binding.chkMicro.isChecked)
        App.prefs.getBoolean("blender",binding.chkBlender.isChecked)
        App.prefs.getBoolean("multi",binding.chkMulti.isChecked)
        App.prefs.getBoolean("airfryer",binding.chkAirfryer.isChecked)
        App.prefs.getBoolean("steam",binding.chkSteam.isChecked)
        App.prefs.getBoolean("sous",binding.chkSous.isChecked)
        App.prefs.getBoolean("grill",binding.chkGrill.isChecked)

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
            App.prefs.setBoolena("grill",isChecked)
        }
        binding.chkGrill.setOnCheckedChangeListener { buttonView, isChecked ->
            App.prefs.setBoolena("oven",isChecked)
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