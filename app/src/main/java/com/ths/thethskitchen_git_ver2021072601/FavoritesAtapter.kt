package com.ths.thethskitchen_git_ver2021072601

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ths.thethskitchen_git_ver2021072601.databinding.ItemFavoritesBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.Serializable

//즐겨찾기 어뎁터
class FavoritesAtapter: RecyclerView.Adapter<FavoritesAtapter.Holder>() {
    var helper: SQLiteDBHelper? = null
    var dList = mutableListOf<DList>()
    lateinit var mContext: Context

    inner class Holder(val binding: ItemFavoritesBinding): RecyclerView.ViewHolder(binding.root) {
        private var mDList: DList? = null

        init {
            binding.btnFavoritesDel.setOnClickListener{
                helper?.deleteFavorites(mDList!!)
                dList.remove(mDList)
                notifyDataSetChanged()
            }
        }
        //아이템 세팅
        fun setDList(dList: DList) {
            binding.txtName.text = dList.name
            CoroutineScope(Dispatchers.Main).launch {
                val url = "https://i.ytimg.com/vi/${dList.video}/mqdefault.jpg" //이미지 URL생성
                val bitmap = withContext(Dispatchers.IO) {
                    loadImage(url)
                }
                if (bitmap != null){
                    binding.imgThumb.setImageBitmap(bitmap)
                } else {
                    binding.imgThumb.setImageResource(R.drawable.icon)    //기본 수정예졍
                }
            }
            this.mDList = dList
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesAtapter.Holder {
        val binding = ItemFavoritesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,false)   //아이템 뷰바인딩
        mContext = parent.context

        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val setList = dList[position]
        holder.setDList(setList)

        // 디테일 화면(슬기로운식샤생활)
        holder.itemView.setOnClickListener{
            Intent(mContext, RecommandDetail::class.java).apply {
                putExtra("data", dList[position] as Serializable)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }.run { mContext.startActivity(this) }
        }
    }

    override fun getItemCount(): Int {
        return dList.size
    }


}
