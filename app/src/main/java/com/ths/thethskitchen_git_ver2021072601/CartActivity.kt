package com.ths.thethskitchen_git_ver2021072601

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.skydoves.balloon.*
import com.ths.thethskitchen_git_ver2021072601.databinding.ActivityCartBinding
import com.ths.thethskitchen_git_ver2021072601.databinding.DailogCartAddBinding
import java.time.LocalDateTime

//장바구니
class CartActivity : BaseActivity() {
    val binding by lazy { ActivityCartBinding.inflate(layoutInflater) }
    var adapter = CartAdapter()
    private var dbHelper = SQLiteDBHelper(this,"THsKitchen.db",1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        adapter.helper = dbHelper
        adapter.cartList.addAll(dbHelper.selectCart())
        binding.viewCartList.adapter = adapter
        binding.viewCartList.layoutManager = LinearLayoutManager(this)
//        binding.viewCartList.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))


//          냉장고 아이템 클릭 ---> 입력화면 이동
        binding.btnAddCart.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val dialogAdd = DailogCartAddBinding.inflate(layoutInflater)

            builder.setTitle(this.getString(R.string.cart_add_title))
            builder.setView(dialogAdd.root)

            val alertDailog = builder.create()

//            저장버튼
            dialogAdd.btnCartAdd.setOnClickListener {
                if (dialogAdd.txtCartAddName.text.toString() != ""  ) {
                    val cartList = CartList( 0,
                        dialogAdd.txtCartAddName.text.toString(),
                        dialogAdd.txtCartAddDesc.text.toString(),
                        LocalDateTime.now())
                    dbHelper.insertCart(cartList)
                    Toast.makeText(this,getString(R.string.msg_save_data),
                        Toast.LENGTH_SHORT).show()
                    //리스트뷰 새로고침
                    adapter.cartList.clear()
                    adapter.cartList.addAll(dbHelper.selectCart())
                    adapter.notifyDataSetChanged()
                    alertDailog.dismiss()   //입력화면 종료
                }else{
                    Toast.makeText(this,this.getString(R.string.msg_add_NoName),
                        Toast.LENGTH_SHORT).show()
                }
            }
//            취소버튼
            dialogAdd.btnCartCanc.setOnClickListener {
                alertDailog.dismiss()
            }
            alertDailog.show()
        }

        //냉장고로 이동
        binding.btnToRefrigerator.setOnClickListener {
            val intent = Intent(this, RefrigeratorActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            startActivity(intent)
        }

        //종료
        binding.btnCartExit.setOnClickListener {
            finish()
        }
        //도움말
        if (adapter.cartList.isEmpty()){
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
            setText(getString(R.string.h_c_empty1))
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
            setText(getString(R.string.h_c_empty2))
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
        binding.btnCartExit.showAlignBottom(balloon)
        binding.btnAddCart.showAlignLeft(balloon2)
    }
}