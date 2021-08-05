package com.ths.thethskitchen_git_ver2021072601

import android.R
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
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
import com.ths.thethskitchen_git_ver2021072601.databinding.MainDrawerHeaderBinding
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
//        var header = binding.navi.getHeaderView(0)
//
//        val bindingHeader = MainDrawerHeaderBinding.inflate(layoutInflater)
//        bindingHeader.btnLink.setOnClickListener {
//            Toast.makeText(this,"kajshdkfjhaksd",Toast.LENGTH_LONG).show()
//        }

//            val intent = Intent(Intent.ACTION_VIEW,
//                Uri.parse("https://https://www.youtube.com/channel/UCM_NeHV1e_QZ5dYr-C-TJqA"))
//            startActivity(intent)


        binding.navi.setNavigationItemSelectedListener {
            when(it.itemId){
                binding.navi.menu.getItem(0).itemId -> {    //우리집냉장고
                    val intent = Intent(this, RefrigeratorActivity::class.java)
                    startActivity(intent)
                }
                binding.navi.menu.getItem(1).itemId ->
                {
                    val intent = Intent(this,KitchenActivity::class.java)
                    startActivity(intent)
                }
                binding.navi.menu.getItem(2).itemId ->
                {
                    val intent = Intent(this,CartActivity::class.java)
                    startActivity(intent)
                }
                binding.navi.menu.getItem(3).itemId ->
                {
                    val intent = Intent(this,FavoritesActivity::class.java)
                    startActivity(intent)
                }
                binding.navi.menu.getItem(4).itemId -> Toast.makeText(this,"${it.itemId}",Toast.LENGTH_LONG).show()
                binding.navi.menu.getItem(5).itemId -> Toast.makeText(this,"${it.itemId}",Toast.LENGTH_LONG).show()
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

    override fun onBackPressed() {
        //드로워 메뉴 백버튼
        if (binding.drawer.isDrawerOpen(GravityCompat.START)){
            binding.drawer.closeDrawers()
        }else{
            super.onBackPressed()
        }
    }


}

