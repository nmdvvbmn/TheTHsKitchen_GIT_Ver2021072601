package com.ths.thethskitchen_git_ver2021072601

import android.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.ths.thethskitchen_git_ver2021072601.databinding.ActivityMainBinding
import com.ths.thethskitchen_git_ver2021072601.databinding.FragmentSearchBinding
import java.io.Serializable
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val viewPager = ViewPagerAdapter(this  )
        viewPager.addFragment(SearchFragment())
        viewPager.addFragment(ListFragment())
        binding.viewPager.adapter = viewPager

        binding.navi.setNavigationItemSelectedListener {
            when(it.itemId){
                binding.navi.menu.getItem(0).itemId -> Toast.makeText(this,"${it.itemId}",Toast.LENGTH_LONG).show()
                binding.navi.menu.getItem(1).itemId -> Toast.makeText(this,"${it.itemId}",Toast.LENGTH_LONG).show()
                binding.navi.menu.getItem(2).itemId -> Toast.makeText(this,"${it.itemId}",Toast.LENGTH_LONG).show()
                else -> Toast.makeText(this,"text",Toast.LENGTH_LONG).show()
            }
            return@setNavigationItemSelectedListener true
        }
        binding.btnMenu.setOnClickListener{
            binding.drawer.openDrawer(GravityCompat.START)

        }
    }

    override fun onBackPressed() {
        if (binding.drawer.isDrawerOpen(GravityCompat.START)){
            binding.drawer.closeDrawers()
        }else{
            super.onBackPressed()
        }
    }


}

