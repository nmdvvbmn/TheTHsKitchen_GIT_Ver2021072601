package com.ths.thethskitchen_git_ver2021072601

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.ths.thethskitchen_git_ver2021072601.databinding.ActivityListBinding
import com.ths.thethskitchen_git_ver2021072601.databinding.FragmentListBinding
import java.io.IOException
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var mBinding : FragmentListBinding? = null
    private val binding get() = mBinding!!
    val db = FirebaseFirestore.getInstance()
    var dlist = arrayListOf<DList>()
    var adapter = RecyclerAdapter(dlist)

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
        mBinding = FragmentListBinding.inflate(inflater,container,false)
        binding.recyclerDList.adapter = adapter
        binding.recyclerDList.layoutManager = LinearLayoutManager(requireContext())

        selectList()



        return binding.root
    }

    private fun selectList() {
        //         firestore 요리 리스트 Query
        val date = UtilFuncs().getKorDate()
        val langCode = UtilFuncs().getLanguage()

        binding.pbLoading.visibility = View.VISIBLE

        db.collection("DList")
            .whereLessThanOrEqualTo("date", date )
            .get().addOnSuccessListener { result ->
                dlist.clear()
                for (document in result.reversed()) {
                    Log.d("DB222",document.id.toString())
                    var item = DList(document.id ,
                        "",
                        document["date"] as Long,
                        document["ptime"] as Long,
                        document["punit"] as String,
                        document["ctime"] as Long,
                        document["cunit"] as String,
                        document["serv"] as Long,
                        document["sunit"] as String,
                        document["start"] as Long,
                        document["stove"] as Long,
                        document["oven"] as Long,
                        document["micro"] as Long,
                        document["blender"] as Long,
                        document["air fryer"] as Long,
                        document["multi cooker"] as Long,
                        document["steamer"] as Long,
                        document["sous vide"] as Long,
                        document["grill"] as Long,
                        document["vdeioID"] as String
                    )

                    // 요리명
                    db.collection("DName")
                        .whereEqualTo("id", document.id).whereEqualTo("code",langCode).get()
                        .addOnSuccessListener { result->
                            for (document in result) {
                                var index = dlist.indexOfFirst { it.id == document["id"] as String }
                                dlist[index].name = document["name"] as String
                                Log.d("DB222", "index ${index}, name ${document["name"]}")
                            }
//                        adapter.notifyDataSetChanged()
                        }.addOnFailureListener {
                            Log.d("DB222","Fail")
                        }.addOnCompleteListener {
                            adapter.notifyDataSetChanged()
                            binding.pbLoading.visibility = View.INVISIBLE
                        }
                    dlist.add(item)
                }

            }.addOnFailureListener { exception ->
                Log.d("test","fail", exception)
            }.addOnCompleteListener {
                adapter.notifyDataSetChanged()
            }
    }

    override fun onDestroyView() {
        mBinding = null
        super.onDestroyView()
    }
    fun isOnline(): Boolean {
        val runtime = Runtime.getRuntime()
        try {
            val ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8")
            val exitValue = ipProcess.waitFor()
            return exitValue == 0
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return false
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}