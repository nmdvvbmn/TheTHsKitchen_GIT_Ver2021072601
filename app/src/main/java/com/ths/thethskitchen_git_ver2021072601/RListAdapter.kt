package com.ths.thethskitchen_git_ver2021072601

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.ths.thethskitchen_git_ver2021072601.databinding.ItemRlistBinding

// 상세패이지 레시피 리스트 어뎁터
class RListAdapter(private val listData: ArrayList<RList>, listener : OnRItem): RecyclerView.Adapter<RListAdapter.Holder>() {
    
    //SeekTo를 위한 인터페이스
    interface OnRItem {
        fun onRItemSelected( time: Float)
    }

    private var mBinding: ItemRlistBinding? = null
    private val binding get() = mBinding!!
    private val mListener = listener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        mBinding = ItemRlistBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding, parent.context)

    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val rlist = listData[position]
        holder.setRList(rlist)
        
        //인터페이스 전달 리스너
        holder.itemView.setOnClickListener{
            mListener.onRItemSelected(rlist.time)
        }

    }

    override fun getItemCount(): Int {
        return listData.size
    }
    inner class Holder(val binding: ItemRlistBinding, val context: Context) : RecyclerView.ViewHolder(binding.root) {
        private var mRList: RList? = null

        @RequiresApi(Build.VERSION_CODES.N)
        fun setRList(rlist: RList) {
            this.mRList = rlist
            binding.txtRName.text = mRList?.name    //재료명
        }
    }
}

