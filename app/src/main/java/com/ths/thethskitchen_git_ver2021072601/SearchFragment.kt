package com.ths.thethskitchen_git_ver2021072601

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.ths.thethskitchen_git_ver2021072601.databinding.FragmentSearchBinding

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
//    var list = arrayListOf<String>()
//    var adapter = KitchenAdapter(list)
    lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        mContext = requireContext()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentSearchBinding.inflate(inflater,container,false)
//        binding.viewSearch.adapter = adapter
//        binding.viewSearch.orientation = ViewPager2.ORIENTATION_HORIZONTAL
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