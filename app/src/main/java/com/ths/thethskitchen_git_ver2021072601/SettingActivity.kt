package com.ths.thethskitchen_git_ver2021072601

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ths.thethskitchen_git_ver2021072601.databinding.ActivitySettingBinding

//설정 화면
class SettingActivity : BaseActivity() {
    val binding by lazy { ActivitySettingBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        //종료
        binding.btnSetExit.setOnClickListener {
            finish()
        }
    }



}