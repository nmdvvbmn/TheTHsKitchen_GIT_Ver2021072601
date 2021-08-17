package com.ths.thethskitchen_git_ver2021072601

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import com.ths.thethskitchen_git_ver2021072601.databinding.ItemHistoryBinding

class HistoryAdapter(val mContext: Context, resource: Int, val list: MutableList<SearchList>):
ArrayAdapter<SearchList>(mContext, resource, list)  , Filterable {
    lateinit var binding: ItemHistoryBinding
    private lateinit var db: SQLiteDBHelper
    var filtered = list
    override fun getCount(): Int {
        return filtered.size
    }

    override fun getItem(position: Int): SearchList {
        return filtered[position]
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        db = SQLiteDBHelper(context,App.dbName,App.dbVer)

        binding.txtHistory.text = filtered[position].tag
        if (filtered[position].flag == 1L) {
            binding.imgHistory.visibility = View.INVISIBLE
            binding.btnDelete.visibility = View.INVISIBLE
        }

        binding.btnDelete.setOnClickListener {
            db.deleteSearch(filtered[position])
            filtered.removeAt(position)
            notifyDataSetChanged()
        }

        return binding.root
    }

    override fun getFilter(): Filter {
        return mfilter
    }
    private var mfilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results = FilterResults()
            val query = if (constraint != null && constraint.isNotEmpty()) {
                autocomplete(constraint.toString())
            }else{
                arrayListOf()
            }
            results.values = query
            results.count = query.size
            return  results
        }

        private  fun autocomplete(input: String): MutableList<SearchList> {
            val results = arrayListOf<SearchList>()
            for (i in list) {
                if (i.tag.lowercase().contains(input.lowercase())){
                    results.add(i)
                }
            }
            return  results
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence?, results: FilterResults) {
            if (results.count > 0) {
                filtered = results.values as MutableList<SearchList>
                notifyDataSetChanged()
            } else{
                notifyDataSetInvalidated()
            }
        }
    }

//    fun getObject(position: Int) = list[position]
//
//    fun setData(changedList: List<SearchList>){
//        list.clear()
//        list.addAll(changedList)
//    }

}