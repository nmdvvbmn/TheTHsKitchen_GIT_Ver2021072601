package com.ths.a20210713_sqllitehelper

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.ths.thethskitchen_git_ver2021072601.databinding.ItemIlistBinding


class IListAdapter: RecyclerView.Adapter<IListAdapter.Holder>() {
    var listData = mutableListOf<IList>()
//    var helper : SqliteHelper? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemIlistBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding, parent.context)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val ilist = listData.get(position)
        holder.setIList(ilist)
    }

    override fun getItemCount(): Int {
        return listData.size
    }
    inner class Holder(val binding: ItemIlistBinding, context: Context ) : RecyclerView.ViewHolder(binding.root) {
        var mIList: IList? = null

        init {
            binding.btnAdd.setOnClickListener(){
                Toast.makeText(context, "Add to Cart",Toast.LENGTH_LONG).show()
            }
        }
        @RequiresApi(Build.VERSION_CODES.N)
        fun setIList(ilist: IList) {
            this.mIList = ilist
            binding.txtName.text = "${ilist.name}"
        }
    }
}

