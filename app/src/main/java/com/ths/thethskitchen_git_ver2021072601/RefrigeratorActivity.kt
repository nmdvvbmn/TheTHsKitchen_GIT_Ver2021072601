package com.ths.thethskitchen_git_ver2021072601

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.skydoves.balloon.*
import com.ths.thethskitchen_git_ver2021072601.databinding.ActivityRefrigeratorBinding
import com.ths.thethskitchen_git_ver2021072601.databinding.DailogRefrigeratorAddBinding
import java.time.LocalDateTime

//냉장고 리스트
class RefrigeratorActivity : BaseActivity() {
    val binding by lazy { ActivityRefrigeratorBinding.inflate(layoutInflater) }
    var adapter = RefrigeratorAtapter()
    private var dbHelper = SQLiteDBHelper(this,"THsKitchen.db", 1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        adapter.helper = dbHelper
        adapter.refrigeratorList.addAll(dbHelper.selectRefrigerator())
        binding.viewRefrigeratorList.adapter = adapter
        binding.viewRefrigeratorList.layoutManager = LinearLayoutManager(this)
//        binding.viewRefrigeratorList.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

//          냉장고 아이템 클릭 ---> 입력화면 이동
        binding.btnAddRefrigerator.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val dialogAdd = DailogRefrigeratorAddBinding.inflate(layoutInflater)
            
            builder.setTitle(this.getString(R.string.refrigerator_add_title))
            builder.setView(dialogAdd.root)
            val alertDailog = builder.create()

            
//            저장버튼
            dialogAdd.btnRefrigeratorAdd.setOnClickListener {
                if (dialogAdd.txtRefrigeratorAddName.text.toString() != ""  ) {
                    val refrigeratorList = RefrigeratorList( 0,
                        dialogAdd.txtRefrigeratorAddName.text.toString(),
                        dialogAdd.txtRefrigeratorAddDesc.text.toString(),
                        LocalDateTime.now())
                    dbHelper.insertRefiregierator(refrigeratorList)
                    Toast.makeText(this,getString(R.string.msg_save_data),
                        Toast.LENGTH_SHORT).show()
                    //리스트뷰 새로고침
                    adapter.refrigeratorList.clear()
                    adapter.refrigeratorList.addAll(dbHelper.selectRefrigerator())
                    adapter.notifyDataSetChanged()
                    alertDailog.dismiss()   //입력화면 종료
                }else{
                    Toast.makeText(this,this.getString(R.string.msg_add_NoName),
                        Toast.LENGTH_SHORT).show()
                }
            }
//            취소버튼
            dialogAdd.btnRefrigeratorCanc.setOnClickListener {
                alertDailog.dismiss()
            }
            alertDailog.show()
        }
//      장바구니 이동
        binding.btnToCart2.setOnClickListener {
            val intent = Intent(this,CartActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            startActivity(intent)
        }
//      종료
        binding.btnRExit.setOnClickListener {
            finish()
        }

        if (adapter.refrigeratorList.isEmpty()) {
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
            setText(getString(R.string.h_r_empty1))
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

        val balloon2 = createBalloon(this) {
            setArrowSize(10)
            setWidth(BalloonSizeSpec.WRAP)
            setHeight(BalloonSizeSpec.WRAP)
            setArrowPosition(0.5f)
            setCornerRadius(4f)
            setAlpha(0.9f)
            setPadding(10)
            setMarginBottom(15)
            setMarginRight(8)
            setTextSize(14.0f)
            setAutoDismissDuration(5000L)
            setText(getString(R.string.h_r_empty2))
            setTextColorResource(R.color.white)
            setTextIsHtml(true)
            arrowOrientation = ArrowOrientation.RIGHT
            setBackgroundColorResource(R.color.thscolor)
            setBalloonAnimation(BalloonAnimation.FADE)
            setLifecycleOwner(lifecycleOwner)
        }
        balloon2.setOnBalloonClickListener {
            balloon2.dismiss()
        }
        binding.btnRExit.showAlignBottom(balloon)
        binding.btnAddRefrigerator.showAlignLeft(balloon2)
    }
}