package com.ths.thethskitchen_git_ver2021072601

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.ths.thethskitchen_git_ver2021072601.databinding.ActivityCartBinding
import com.ths.thethskitchen_git_ver2021072601.databinding.DailogCartAddBinding
import com.ths.thethskitchen_git_ver2021072601.databinding.DailogRefrigeratorAddBinding
import java.time.LocalDateTime

//장바구니
class CartActivity : AppCompatActivity() {
    val binding by lazy { ActivityCartBinding.inflate(layoutInflater) }
    var adapter = CartAdapter()
    var dbHelper = SQLiteDBHelper(this,"THsKitchen.db",1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        adapter.helper = dbHelper
        adapter.cartList.addAll(dbHelper.select_cart())
        binding.viewCartList.adapter = adapter
        binding.viewCartList.layoutManager = LinearLayoutManager(this)


//          냉장고 아이템 클릭 ---> 입력화면 이동
        binding.btnAddCart.setOnClickListener {
            var builder = AlertDialog.Builder(this)
            var dialogAdd = DailogCartAddBinding.inflate(layoutInflater)

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
                    dbHelper.insert_cart(cartList)
                    Toast.makeText(this,getString(R.string.msg_save_data),
                        Toast.LENGTH_SHORT).show()
                    //리스트뷰 새로고침
                    adapter.cartList.clear()
                    adapter.cartList.addAll(dbHelper.select_cart())
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
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            startActivity(intent)
        }

        //종료
        binding.btnCartExit.setOnClickListener {
            finish()
        }
    }
}