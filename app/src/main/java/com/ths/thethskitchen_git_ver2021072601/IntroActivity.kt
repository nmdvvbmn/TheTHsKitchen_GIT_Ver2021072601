package com.ths.thethskitchen_git_ver2021072601

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.animation.AnimationUtils
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenResumed
import com.ths.thethskitchen_git_ver2021072601.databinding.ActivityIntroBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class IntroActivity : AppCompatActivity() {
    val binding by lazy{ActivityIntroBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.imgIntro.startAnimation(AnimationUtils.loadAnimation(applicationContext,R.anim.rotate))

        lifecycleScope.launch {
            whenResumed {
                    delay(1500)
                    val colorChange = AnimatorInflater.loadAnimator(applicationContext,R.anim.color) as AnimatorSet
                    colorChange.setTarget(binding.viewIntro)
                    colorChange.start()
                    delay(1500)
                    colorChange.end()
                    val intent = Intent(baseContext, MainActivity::class.java)
                    startActivity(intent)
                    finish()
            }
        }
    }
}
