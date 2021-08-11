package com.ths.thethskitchen_git_ver2021072601

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenResumed
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.firestore.FirebaseFirestore
import com.ths.thethskitchen_git_ver2021072601.databinding.FragmentSearchBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var mBinding: FragmentSearchBinding? =  null
    private val binding get() = mBinding!!
    var list = arrayListOf<String>()
    var adapter = KitchenAdapter(list)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentSearchBinding.inflate(inflater,container,false)
        binding.viewSearch.adapter = adapter
        binding.viewSearch.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.btnSearch.setOnClickListener{
            if (binding.editSearch.text.toString() != ""){
                Intent(context, ListActivity::class.java).apply {
                    putExtra("search", binding.editSearch.text.toString())
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run {context?.startActivity(this) }
            } else {
                Toast.makeText(context,"검색어를 입력하세요", Toast.LENGTH_SHORT).show()
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            val db = FirebaseFirestore.getInstance()
            db.collection("URList").get().addOnSuccessListener { result ->
                for (document in result) {
                    val URL = document["URL"] as String
                    list.add(URL)
                }
                Log.d("ListAdapter","Coroutine")
//                withContext(Dispatchers.Main){
//                    Log.d("ListAdapter","${list.size}")
//                    Log.d("ListAdapter","Dispatchers")
                adapter.notifyDataSetChanged()
//                }
            }
        }
        //        //이미지 로딩을 위한 스레드
        var isRunning = true
        lifecycleScope.launch {
            whenResumed {
                while (isRunning) {
                    delay(3000)
                    if (list.size > 0){
                        binding.viewSearch.currentItem?.let {
                            binding.viewSearch.setCurrentItem(it.plus(1)  )
                        }
                    }
                }
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        mBinding = null
        super.onDestroyView()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SearchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}