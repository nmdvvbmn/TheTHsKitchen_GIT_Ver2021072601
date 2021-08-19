package com.ths.thethskitchen_git_ver2021072601

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.skydoves.balloon.*
import com.ths.thethskitchen_git_ver2021072601.databinding.ActivityFavoritesBinding

//즐겨찾기
class FavoritesActivity : BaseActivity() {
    val binding by lazy { ActivityFavoritesBinding.inflate(layoutInflater) }
    var adapter = FavoritesAtapter()
    private var dbHelper = SQLiteDBHelper(this,App.dbName, App.dbVer)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        adapter.helper = dbHelper
        adapter.dList.addAll(dbHelper.selectFavorites())
        binding.viewFavoritesList.adapter = adapter
        binding.viewFavoritesList.layoutManager = LinearLayoutManager(this)


        //광고
        MobileAds.initialize(this)
        val adRequest = AdRequest.Builder().build()
        binding.adViewF.loadAd(adRequest)
        binding.adViewF.adListener = object: AdListener() {
            override fun onAdClicked() {
                App.ad = false
                super.onAdClicked()
            }
        }

        //종료
        binding.btnFExit.setOnClickListener {
            finish()
        }
        if (adapter.dList.isEmpty()&& App.prefs.getBoolean("help",true)){
            createHelp()
        }

    }

    private fun createHelp() {
        val balloon = createBalloon(this) {
            setArrowSize(10)
            setWidth(BalloonSizeSpec.WRAP)
            setHeight(BalloonSizeSpec.WRAP)
            setArrowPosition(0.1f)
            setCornerRadius(4f)
            setAlpha(0.9f)
            setPadding(10)
            setMarginTop(15)
            setMarginLeft(8)
            setTextSize(14.0f)
            setAutoDismissDuration(5000L)
            setText(getString(R.string.h_f_empty1))
            setTextColorResource(R.color.white)
            setTextIsHtml(true)
            arrowOrientation = ArrowOrientation.BOTTOM
            setBackgroundColorResource(R.color.thscolor)
            setBalloonAnimation(BalloonAnimation.FADE)
            setLifecycleOwner(lifecycleOwner)
        }
        balloon.setOnBalloonClickListener {
            balloon.dismiss()
        }

        binding.btnFExit.showAlignBottom(balloon)

    }
}