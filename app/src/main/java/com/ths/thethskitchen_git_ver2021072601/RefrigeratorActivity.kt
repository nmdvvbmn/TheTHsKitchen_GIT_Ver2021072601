package com.ths.thethskitchen_git_ver2021072601

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.ths.thethskitchen_git_ver2021072601.databinding.ActivityRefrigeratorBinding
import com.ths.thethskitchen_git_ver2021072601.databinding.DailogRefrigeratorAddBinding
import java.time.LocalDateTime

//냉장고 리스트
class RefrigeratorActivity : AppCompatActivity() {
    val binding by lazy { ActivityRefrigeratorBinding.inflate(layoutInflater) }
    var adapter = RefrigeratorAtapter()
    var dbHelper = RefrigeratorHelper(this,"THsKitchen.db", 1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        adapter.helper = dbHelper
        adapter.refrigeratorList.addAll(dbHelper.select())
        binding.viewRefrigeratorList.adapter = adapter
        binding.viewRefrigeratorList.layoutManager = LinearLayoutManager(this)

//          냉장고 아이템 클릭 ---> 입력화면 이동
        binding.btnAddRefrigerator.setOnClickListener {
            var builder = AlertDialog.Builder(this)
            var dialogAdd = DailogRefrigeratorAddBinding.inflate(layoutInflater)
            
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
                    dbHelper.insert(refrigeratorList)
                    Toast.makeText(this,getString(R.string.msg_save_data),
                        Toast.LENGTH_SHORT).show()
                    //리스트뷰 새로고침
                    adapter.refrigeratorList.clear()
                    adapter.refrigeratorList.addAll(dbHelper.select())
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

    }
}