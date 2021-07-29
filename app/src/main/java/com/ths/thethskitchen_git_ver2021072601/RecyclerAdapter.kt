package com.ths.thethskitchen_git_ver2021072601

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.ths.thethskitchen_git_ver2021072601.databinding.ItemRecyclerBinding
import kotlinx.coroutines.*
import java.io.FileNotFoundException
import java.io.Serializable
import java.net.URL

suspend fun loadImage(imageUrl: String): Bitmap? {
    val url = URL(imageUrl)

    try {
        val stream = url.openStream()
        return  BitmapFactory.decodeStream(stream)
    } catch (e: FileNotFoundException){
        return  null
    }

}

class RecyclerAdapter(val listData: ArrayList<DList> ):RecyclerView.Adapter<RecyclerAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemRecyclerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding, parent.context )
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val dlist = listData.get(position)
        holder.setDList(dlist)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    inner class Holder(val binding: ItemRecyclerBinding, context: Context) : RecyclerView.ViewHolder(binding.root) {
        var mdlist: DList? = null

        init {
            binding.root.setOnClickListener {
                Intent(context, RecommandDetail::class.java).apply {
                    putExtra("data", listData.get(adapterPosition) as Serializable)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run { context.startActivity(this) }

            }
//            binding.btnDel.setOnClickListener {
//                Log.d("SQL","DEL_Click")
//                listData.remove(mdlist)
//                notifyDataSetChanged()
//            }
        }
        @RequiresApi(Build.VERSION_CODES.N)
        fun setDList(dlist: DList) {
            this.mdlist = dlist
            binding.txtName.text = "${dlist.name}"
            CoroutineScope(Dispatchers.Main).launch {
                val url = "https://i.ytimg.com/vi/${dlist.video}/0.jpg"
                val bitmap = withContext(Dispatchers.IO) {
                    loadImage(url)
                }
                if (bitmap != null){
                    binding.imgThumb.setImageBitmap(bitmap)
                } else {
                    binding.imgThumb.setImageResource(R.drawable.ic_launcher_background)
                }
            }

        }
    }
}


