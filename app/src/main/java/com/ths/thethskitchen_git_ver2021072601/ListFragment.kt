package com.ths.thethskitchen_git_ver2021072601

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
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
    var proc = false

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
        binding.swipeLayout.setOnRefreshListener {
            if (proc == false){
                selectList()
                binding.swipeLayout.isRefreshing = false
            }else{
                binding.swipeLayout.isRefreshing = false
            }
        }
        //데이터 조회
        selectList()
        return binding.root
    }

    private fun selectList() {
        //         firestore 요리 리스트 Query
        val date = UtilFuncs().getKorDate()
        val langCode = UtilFuncs().getLanguage()
        val helper = SQLiteDBHelper(requireContext(), "THsKitchen.db", 1)
        var refrigerator = helper.select_refrigerator()
        var nameList: ArrayList<String> = arrayListOf()
        var idList: ArrayList<String> = arrayListOf()

        var dlistCnt = 0    //DList sync 위한 카운터
        var dnameCnt = 0    //DName Sync 위한 카운터

        //나의 부엌 Preference 
        val stove = getTooltoInt(App.prefs.getBoolean("stove", false))
        val oven = getTooltoInt(App.prefs.getBoolean("oven", false))
        val micro = getTooltoInt(App.prefs.getBoolean("micro", false))
        val blender = getTooltoInt(App.prefs.getBoolean("blender", false))
        val multi = getTooltoInt(App.prefs.getBoolean("multi", false))
        val airfryer = getTooltoInt(App.prefs.getBoolean("airfryer", false))
        val steam = getTooltoInt(App.prefs.getBoolean("steam", false))
        val sous = getTooltoInt(App.prefs.getBoolean("sous", false))
        val grill = getTooltoInt(App.prefs.getBoolean("grill", false))
        
        for (i in 0..refrigerator.size - 1) {   //냉장고 저장 리스트
            nameList.add(refrigerator[i].name)
        }

        val nameList_d = nameList.distinct()        //재료 중복 제거

        if (nameList_d.size <= 0) {     // 재료 없음 검색 X
            binding.pbLoading.visibility = View.INVISIBLE
        }else{ // 초기화
            dlist.clear()
            proc = true
            binding.pbLoading.visibility = View.VISIBLE
            dlistCnt = 0
            dnameCnt = 0
        }

        for (i in 0..nameList_d.size - 1) { //IName에서 DList ID 검색
            db.collection("IName")
                .whereEqualTo("name", nameList_d[i])
                .get().addOnSuccessListener { result ->
                    for (document in result) {
                        idList.add(document["id"] as String)    //ID list
                    }
                }.addOnFailureListener {
                    Log.d("Refrigerator&Dname", "Fail")
                }.addOnCompleteListener { task ->
                    dlistCnt ++
                    if (dlistCnt  == nameList_d.size ) { //ID list 검색 완료
                        val idList_d = idList.distinct().sortedDescending() //Id List 중복 제거 정렬
                        if (task.isSuccessful && idList_d.size > 0) { //Id list가 있을 경우
                            for (i in 0..idList_d.size - 1) {
                                db.collection("DList").document(idList_d[i])
                                    .get().addOnSuccessListener { document ->
                                        var item = DList(   //DList Row
                                            document.id,
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
                                            document["vdeioID"] as String,
                                            ""
                                        )

                                        if (item.date <= date   // 날짜 및 나의 부엌 조회조건
                                            && item.stove <= stove
                                            && item.oven <= oven
                                            && item.micro <= micro
                                            && item.blender <= blender
                                            && item.multi <= multi
                                            && item.airfryer <= airfryer
                                            && item.steamer <= steam
                                            && item.sousvide <= sous
                                            && item.grill <= grill ) {
                                            dlist.add(item)

                                            // 요리명
                                            db.collection("DName")
                                                .whereEqualTo("id", idList_d[i])
                                                .whereEqualTo("code", langCode)
                                                .limit(1)
                                                .get()
                                                .addOnSuccessListener { result ->
                                                    for (document in result) {
                                                        var index =
                                                            dlist.indexOfFirst { it.id == document["id"] as String }
                                                        dlist[index].name =
                                                            document["name"] as String
                                                    }
                                                    //Dname 실패(언어코드 확인, 데이터 없음)
                                                }.addOnFailureListener {
                                                    Log.d("DB222", "Fail")
                                                }.addOnCompleteListener {
                                                    dnameCnt++
                                                    if (dnameCnt == idList_d.size) {
                                                        //마지막 리턴 후 프로그레스 종료
                                                        binding.pbLoading.visibility =
                                                            View.INVISIBLE
                                                        //새로고침플레그 해제
                                                        proc = false
                                                    }
                                                    //리스트뷰 업데이트 (중간중간 데이터 띄우기 위함)
                                                    adapter.notifyDataSetChanged()
                                                }
                                            }else{
                                                dnameCnt++
                                                if (dnameCnt == idList_d.size) {
                                                    //실패시 마지막 프로그레스 종료
                                                    binding.pbLoading.visibility =
                                                        View.INVISIBLE
                                                    //최종 완료 리스트 뷰 적용
                                                    adapter.notifyDataSetChanged()
                                                    // 새로고침 플레그 해제
                                                    proc = false
                                                }
                                            }
                                        }.addOnFailureListener { exception ->
                                            dnameCnt++
//                                            if (dnameCnt == idList_d.size) {
                                            if (dlistCnt == idList_d.size) {
                                                binding.pbLoading.visibility =
                                                    View.INVISIBLE
                                                proc = false
                                            }
                                        }.addOnCompleteListener {
                                        adapter.notifyDataSetChanged()
                                    }
                                }
                            } else {
                                binding.pbLoading.visibility = View.INVISIBLE
                                proc = false
                            }
                        }
                    }

            }
    }
    // 나의 부엌 체크박스를 변환
    private fun getTooltoInt(boolean: Boolean): Int {
        if (boolean) {
            return 2
        } else {
            return 1
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