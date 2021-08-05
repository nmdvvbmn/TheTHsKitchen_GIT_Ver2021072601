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
import kotlinx.coroutines.currentCoroutineContext
import java.time.LocalDateTime


class IListAdapter(val listData: ArrayList<IList>): RecyclerView.Adapter<IListAdapter.Holder>() {
    var mBinding: ItemIlistBinding? = null
    private val binding get() = mBinding!!
    var helper: SQLiteDBHelper? = null
    var dlist: DList? = null
    var mContext: Context? = null
    private val context get() = mContext!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        mBinding = ItemIlistBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        mContext = parent.context
        helper = SQLiteDBHelper(context,"THsKitchen.db",1)
        return Holder(binding, parent.context)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val ilist = listData.get(position)
//        val binding = binding
        holder.setIList(ilist)

        binding.btnRAddCart.setOnClickListener {
            var desc = "${dlist?.name}\n" +
                    "${context.getString(R.string.qunt)} : ${UtilFuncs().floatFormat(ilist.qunt)} ${ilist.quntunit} "
            if (ilist.need == false) {
                desc = desc + "${context.getString(R.string.option)}"
            }
            val cartItem = CartList(0,ilist.name!!, desc, LocalDateTime.now() )
            helper?.insert_cart(cartItem)
            Toast.makeText(context,context.getString(R.string.msg_save_data),
                Toast.LENGTH_SHORT).show()
            holder.binding.btnRAddCart.visibility = View.INVISIBLE
//            notifyDataSetChanged()
        }

    }

    override fun getItemCount(): Int {
        return listData.size
    }
    inner class Holder(val binding: ItemIlistBinding, context: Context ) : RecyclerView.ViewHolder(binding.root) {
        var mIList: IList? = null
        val context = context

        init {
//            binding.btnAdd.setOnClickListener(){
//                Toast.makeText(context, "Add to Cart",Toast.LENGTH_LONG).show()
//            }
        }
        @RequiresApi(Build.VERSION_CODES.N)
        fun setIList(ilist: IList) {
            this.mIList = ilist
            var txt: String = "${ilist.name} "
            if (ilist.qunt > 0){
                txt = txt + " " + UtilFuncs().floatFormat(ilist.qunt)
            }
            if (ilist.quntunit != "-" ){
                txt = txt + " ${ilist.quntunit}"
            }

            if (ilist.need) {
                binding.chkName.setTypeface(null, Typeface.BOLD)
            }else{
                txt = txt + " " + context.getString(R.string.option)
            }

            binding.chkName.text = txt

        }
    }
}

