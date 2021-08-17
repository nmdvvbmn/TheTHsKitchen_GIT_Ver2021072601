package com.ths.thethskitchen_git_ver2021072601

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.ths.thethskitchen_git_ver2021072601.databinding.ItemIlistBinding
import java.time.LocalDateTime

// 디테일(슬기로운식샤생활)-재료 어뎁터
class IListAdapter(private val listData: ArrayList<IList>): RecyclerView.Adapter<IListAdapter.Holder>() {
    private var mBinding: ItemIlistBinding? = null
    private val binding get() = mBinding!!
    var helper: SQLiteDBHelper? = null
    var dlist: DList? = null
    var mContext: Context? = null
    val tempList = arrayListOf<String>()
    private val context get() = mContext!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        mBinding = ItemIlistBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        mContext = parent.context
        helper = SQLiteDBHelper(context,App.dbName,App.dbVer)
        return Holder(binding, parent.context)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val ilist = listData[position]
        holder.setIList(ilist)

        // 장바구니 추가
        binding.btnRAddCart.setOnClickListener {
            val list = listData[holder.layoutPosition]

            var desc = "${dlist?.name}\n" + // 요리명 + 사용량
                    "${context.getString(R.string.qunt)} : ${UtilFuncs().floatFormat(list.qunt)} ${list.quntunit}"

            if (!list.need) {  //옵션재료
                desc += context.getString(R.string.option)
            }

            desc += "\n${mContext?.getString(R.string.inputdate)} : " +
                    StringFuncs().makeDateString(LocalDateTime.now()) +
                    " (${mContext?.getString(R.string.menu_cart)})"

            val cartItem = CartList(0,list.name!!, desc, LocalDateTime.now() )
            helper?.insertCart(cartItem)
            helper?.insertSearch(cartItem.name,1)
            Toast.makeText(context,context.getString(R.string.msg_save_data),
                Toast.LENGTH_SHORT).show()
            tempList.add(list.name!!)
            //프로그레스바
            holder.binding.btnRAddCart.visibility = View.INVISIBLE
        }

    }


    override fun getItemCount(): Int {
        return listData.size
    }


    inner class Holder(val binding: ItemIlistBinding, val context: Context) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var mIList: IList

        @RequiresApi(Build.VERSION_CODES.N)
        fun setIList(ilist: IList) {
            this.mIList = ilist
            
            var txt = "${ilist.name} "//재료명
            
            // 사용량이 0보다 클때만 
            if (ilist.qunt > 0){
                txt = txt + " " + UtilFuncs().floatFormat(ilist.qunt)
            }
            //사용량 단위
            if (ilist.quntunit != "-" ){
                txt += " ${ilist.quntunit}"
            }
            // 선택재료 추가 택스트
            if (ilist.need) {
                binding.chkName.setTypeface(null, Typeface.BOLD)
            }else{
                txt = txt + " " + context.getString(R.string.option)
            }
            binding.chkName.text = txt

            when {
                helper?.existsRefiregieratorItem(ilist.name.toString())==true -> {
                    binding.btnRAddCart.visibility = View.INVISIBLE
                }
                tempList.indexOf(ilist.name) >= 0 -> {
                    binding.btnRAddCart.visibility = View.INVISIBLE
                }
                else -> {
                    binding.btnRAddCart.visibility = View.VISIBLE
                }
            }
        }
    }
}

