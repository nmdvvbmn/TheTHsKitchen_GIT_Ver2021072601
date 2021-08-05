package com.ths.thethskitchen_git_ver2021072601

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.ths.thethskitchen_git_ver2021072601.databinding.ItemRlistBinding
import kotlinx.coroutines.flow.callbackFlow


class RListAdapter(val listData: ArrayList<RList>, listener : OnRItem): RecyclerView.Adapter<RListAdapter.Holder>() {

    interface OnRItem {
        fun onRItemSelected( time: Float)
    }

    var mBinding: ItemRlistBinding? = null
    private val binding get() = mBinding!!
    val mListener = listener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        mBinding = ItemRlistBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding, parent.context)

    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val rlist = listData.get(position)
        holder.setRList(rlist)

        holder.itemView.setOnClickListener{
            mListener.onRItemSelected(rlist.time)
        }

    }

    override fun getItemCount(): Int {
        return listData.size
    }
    inner class Holder(val binding: ItemRlistBinding, context: Context ) : RecyclerView.ViewHolder(binding.root) {
        var mRList: RList? = null
        val context = context

        @RequiresApi(Build.VERSION_CODES.N)
        fun setRList(rlist: RList) {
            this.mRList = rlist
            binding.txtRName.text = mRList?.name

        }
    }
}

