package com.ths.thethskitchen_git_ver2021072601

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.ths.thethskitchen_git_ver2021072601.databinding.ItemIlistBinding


class IListAdapter(val listData: ArrayList<IList>): RecyclerView.Adapter<IListAdapter.Holder>() {
    var mBinding: ItemIlistBinding? = null
    private val binding get() = mBinding!!
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        mBinding = ItemIlistBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding, parent.context)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val ilist = listData.get(position)
        holder.setIList(ilist)

        binding.btnRAddCart.setOnClickListener {

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

