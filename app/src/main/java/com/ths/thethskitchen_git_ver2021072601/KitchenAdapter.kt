package com.ths.thethskitchen_git_ver2021072601

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.ths.thethskitchen_git_ver2021072601.databinding.ItemKitchenBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ArithmeticException

class KitchenAdapter(val list: ArrayList<String>):RecyclerView.Adapter<KitchenAdapter.Holder>() {
    lateinit var mContext: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemKitchenBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        mContext = parent.context
        return Holder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: Holder, position: Int) {
        var url = ""
        val pos: Int = try {
            position % list.size
        }catch (e: ArithmeticException){
            0
        }

        holder.itemView.layoutParams
        holder.itemView.requestLayout()
        holder.setIsRecyclable(false)

        if (list.size > 0){
            url = list[pos]
            holder.setList(url)
        }
        //아이템 클릭
        holder.itemView.setOnClickListener{
//            --> 디테일로 이동
            val intent = Intent( Intent.ACTION_VIEW,
                Uri.parse("https://www.youtube.com/watch?v=${url}"))
            startActivity(mContext,intent,null)
        }
    }

    override fun getItemCount(): Int {
        return Integer.MAX_VALUE
    }

    inner class Holder(val binding: ItemKitchenBinding) : RecyclerView.ViewHolder(binding.root) {
        var mdlist: DList? = null
        fun setList(URL: String){
            CoroutineScope(Dispatchers.Main).launch {
                val url = "https://i.ytimg.com/vi/${URL}/0.jpg" //이미지 URL생성
//                Log.d("ListAdapter","${URL}")
                val bitmap = withContext(Dispatchers.IO) {
                    loadImage(url)
                }
                if (bitmap != null){
                    binding.imgReview.setImageBitmap(bitmap)
                }else{
                    binding.imgReview.setImageResource(R.drawable.ic_launcher_background)
                }
            }
        }

    }
}


