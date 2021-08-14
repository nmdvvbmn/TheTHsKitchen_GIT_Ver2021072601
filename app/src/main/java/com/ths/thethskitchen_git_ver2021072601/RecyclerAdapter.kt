package com.ths.thethskitchen_git_ver2021072601

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.ths.thethskitchen_git_ver2021072601.databinding.ItemRecyclerBinding
import kotlinx.coroutines.*
import java.io.Serializable
import java.util.*


// 추천 요리 어뎁터
class RecyclerAdapter(private val listData: ArrayList<DList>):RecyclerView.Adapter<RecyclerAdapter.Holder>() {
    lateinit var mContext: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemRecyclerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        mContext = parent.context
        return Holder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val dlist = listData[position]
        val layoutParams = holder.itemView.layoutParams
        layoutParams.height = 400
        holder.itemView.requestLayout()
        holder.setIsRecyclable(false)
        holder.setDList(dlist)
        //아이템 클릭
        holder.itemView.setOnClickListener{
//            --> 디테일로 이동
            Intent(mContext, RecommandDetail::class.java).apply {
                putExtra("data", listData[position] as Serializable)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }.run { mContext.startActivity(this) }
        }
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    inner class Holder(val binding: ItemRecyclerBinding) : RecyclerView.ViewHolder(binding.root) {
        private var mdlist: DList? = null

        @SuppressLint("UseCompatLoadingForDrawables")
        @RequiresApi(Build.VERSION_CODES.N)

//        리스트 아이템 세팅
        fun setDList(dlist: DList) {
            this.mdlist = dlist
            binding.txtName.text = "${dlist.name}"
            CoroutineScope(Dispatchers.Main).launch {
                val url = "https://i.ytimg.com/vi/${dlist.video}/0.jpg" //이미지 URL생성
                val bitmap = withContext(Dispatchers.IO) {
                    loadImage(url)
                }
                if (bitmap != null){
                    binding.imgThumb.setImageBitmap(bitmap)
                } else {
                    binding.imgThumb.setImageResource(R.drawable.ic_launcher_background)    //기본 수정예졍
                }
            }
            if (dlist.id != "" && dlist.desc == ""){
                binding.txtDesc.text = StringFuncs().makeDesc(dlist, mContext)
            } else {
                binding.txtDesc.text = dlist.desc
                binding.txtDesc.maxLines = 3
            }

            when (dlist.flag){
                4 -> {
                    binding.itemList.background = mContext.getDrawable(R.drawable.edge)
                    binding.imgNew.visibility = View.VISIBLE
                }

//                3 -> binding.itemList.background = mContext.getDrawable(R.drawable.edge)
//                2 -> binding.itemList.background = mContext.getDrawable(R.drawable.edge)
//                1 -> binding.itemList.background = mContext.getDrawable(R.drawable.edge_line)
//                0 -> binding.itemList.background = mContext.getDrawable(R.drawable.edge_line)
            }

        }
    }
}


