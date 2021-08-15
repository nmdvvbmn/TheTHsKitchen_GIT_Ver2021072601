package com.ths.thethskitchen_git_ver2021072601

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.ths.thethskitchen_git_ver2021072601.databinding.ActivityHelpBinding

class HelpActivity : BaseActivity() {
    val binding by lazy { ActivityHelpBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}