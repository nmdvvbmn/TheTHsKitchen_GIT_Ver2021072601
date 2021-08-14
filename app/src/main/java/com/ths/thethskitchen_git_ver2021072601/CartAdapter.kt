package com.ths.thethskitchen_git_ver2021072601

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.ths.thethskitchen_git_ver2021072601.databinding.DailogCartAddBinding
import com.ths.thethskitchen_git_ver2021072601.databinding.ItemCartBinding

class CartAdapter: RecyclerView.Adapter<CartAdapter.Holder>() {
    var helper: SQLiteDBHelper? = null
    var cartList = mutableListOf<CartList>()
    private lateinit var dialogAdd : DailogCartAddBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context),
            parent,false)   //아이템 뷰바인딩
        dialogAdd = DailogCartAddBinding.inflate(LayoutInflater.from(parent.context),
            parent,false)   //다이얼로그 뷰바인딩
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val setList = cartList[position]
        holder.setCartList(setList)
        val layoutParam = holder.itemView.layoutParams
        layoutParam.height = 200
        holder.itemView.requestLayout()
        //아이템 클릭 --> 수정화면
        holder.itemView.setOnClickListener{ it ->
            val builder = AlertDialog.Builder(it.context)
//            builder.setTitle(it.context.getString(R.string.cart_update_title))

//            오류 예방
            if (dialogAdd.root.parent != null){
                (dialogAdd.root.parent as ViewGroup).removeView(dialogAdd.root)
            }

            builder.setView(dialogAdd.root)
            dialogAdd.txtCartAddName.setText(setList.name)
            dialogAdd.txtCartAddDesc.setText(setList.desc)

            val alertDailog = builder.create()
//          저장 버튼
            dialogAdd.btnCartAdd.setOnClickListener { 
                if (dialogAdd.txtCartAddName.text.toString() != ""  ) {
                    setList.name = dialogAdd.txtCartAddName.text.toString()
                    setList.desc = dialogAdd.txtCartAddDesc.text.toString()
                    helper?.updateCart(setList)
                    Toast.makeText(it.context, it.context.getString(R.string.msg_save_data),
                        Toast.LENGTH_SHORT).show()
                    alertDailog.dismiss()
                    cartList[position] = setList
                    notifyDataSetChanged()
                }else{
                    Toast.makeText(it.context,it.context.getString(R.string.msg_add_NoName),
                        Toast.LENGTH_SHORT).show()
                }
            }
//          취소번튼
            dialogAdd.btnCartCanc.setOnClickListener {
                alertDailog.dismiss()
            }
            alertDailog.show()
        }
    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    inner class Holder(private val bindng: ItemCartBinding): RecyclerView.ViewHolder(bindng.root) {
        var mCartList: CartList? = null

        init {
            //리스트 재료 삭제
            bindng.btnFavoritesDel.setOnClickListener{
                helper?.deleteCart(mCartList!!)
                cartList.remove(mCartList)
                notifyDataSetChanged()
            }
            //리스트 냉장고로 이동
            bindng.btnMoveRefrigerator.setOnClickListener {
                helper?.moveCart(mCartList!!)
                cartList.remove(mCartList)
                notifyDataSetChanged()
                Toast.makeText(it.context, it.context.getString(R.string.msg_move_data),
                Toast.LENGTH_SHORT).show()
            }
        }
        //아이템 세팅
        fun setCartList(cartList: CartList) {
            this.mCartList = cartList
            bindng.txtCartName.text = cartList.name //재료명
            this.mCartList = cartList
        }
    }
}
