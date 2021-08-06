package com.ths.thethskitchen_git_ver2021072601

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.preference.PreferenceManager
import com.ths.thethskitchen_git_ver2021072601.databinding.ActivitySettingBinding
import java.util.prefs.PreferenceChangeListener

class SettingActivity : AppCompatActivity() {
    val binding by lazy { ActivitySettingBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.btnSetExit.setOnClickListener {
            finish()
        }
    }



}