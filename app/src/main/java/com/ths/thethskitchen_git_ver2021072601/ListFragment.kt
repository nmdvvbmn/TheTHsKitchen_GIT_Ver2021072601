package com.ths.thethskitchen_git_ver2021072601

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.skydoves.balloon.*
import com.ths.thethskitchen_git_ver2021072601.databinding.FragmentListBinding
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
    var dlist = arrayListOf<DList>()
    var adapter = RecyclerAdapter(dlist,0)
    private var proc = false

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
    ): View {
        mBinding = FragmentListBinding.inflate(inflater,container,false)
        binding.recyclerDList.adapter = adapter
        binding.recyclerDList.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerDList.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        val animator = binding.recyclerDList.itemAnimator
        if (animator is SimpleItemAnimator) {
            animator.supportsChangeAnimations = false
        }

        binding.swipeLayout.setOnRefreshListener {
            if (!proc){
                selectList()
                App.changed = false
                binding.swipeLayout.isRefreshing = false
            }else{
                binding.swipeLayout.isRefreshing = false
            }
        }
        //????????? ??????
        selectList()

        return binding.root
    }



    override fun onResume() {
        super.onResume()
        if (App.changed){
            selectList()
        }
    }
    private fun selectList() {
        //         firestore ?????? ????????? Query
        val date = UtilFuncs().getKorDate()
        val langCode = UtilFuncs().getLanguage()
        val helper = SQLiteDBHelper(requireContext(), App.dbName, App.dbVer)
        val refrigerator = helper.selectRefrigerator()
        val nameList: ArrayList<String> = arrayListOf()
        val idList: ArrayList<String> = arrayListOf()
        val seqList: ArrayList<String> = arrayListOf()
        val countList: ArrayList<String> = arrayListOf()

        var dlistCnt = 0    //DList sync ?????? ?????????
        var seqCnt = 0
        var dnameCnt = 0    //DName Sync ?????? ?????????
        App.changed = false
        dlist.clear()
        binding.pbLoading.visibility = View.VISIBLE

        //?????? ?????? Preference
        val stove = getTooltoInt(App.prefs.getBoolean("stove", false))
        val oven = getTooltoInt(App.prefs.getBoolean("oven", false))
        val micro = getTooltoInt(App.prefs.getBoolean("micro", false))
        val blender = getTooltoInt(App.prefs.getBoolean("blender", false))
        val multi = getTooltoInt(App.prefs.getBoolean("multi", false))
        val airfryer = getTooltoInt(App.prefs.getBoolean("airfryer", false))
        val steam = getTooltoInt(App.prefs.getBoolean("steam", false))
        val sous = getTooltoInt(App.prefs.getBoolean("sous", false))
        val grill = getTooltoInt(App.prefs.getBoolean("grill", false))

        for (i in 0 until refrigerator.size) {   //????????? ?????? ?????????
            nameList.add(refrigerator[i].name)
        }

        val nameListD = nameList.distinct()        //?????? ?????? ??????

        for (i in nameListD.size - 1 downTo 30){
            nameList.removeAt(i)
        }

        if (nameListD.isEmpty()) {     // ?????? ?????? ?????? X
            binding.pbLoading.visibility = View.INVISIBLE
            binding.txtNoData.visibility = View.VISIBLE
            adapter.notifyDataSetChanged()
            if(dlist.size == 0){
                val balloon1 = createBalloon(requireContext()) {
                    setArrowSize(0)
                    setWidth(BalloonSizeSpec.WRAP)
                    setHeight(BalloonSizeSpec.WRAP)
                    setArrowPosition(0.5f)
                    setCornerRadius(4f)
                    setAlpha(0.9f)
                    setPadding(10)
                    setMarginTop(70)
//                                setMarginLeft(150)
                    setTextSize(14.0f)
                    setAutoDismissDuration(5000L)
                    setText(getString(R.string.h_recommand))
                    setTextColorResource(R.color.white)
                    setTextIsHtml(true)
                    setArrowOrientation(ArrowOrientation.TOP)
                    setBackgroundColorResource(R.color.thscolor)
                    setBalloonAnimation(BalloonAnimation.FADE)
                    setLifecycleOwner(lifecycleOwner) }
                balloon1.setOnBalloonClickListener {
                    balloon1.dismiss()
                }
                binding.root.showAlignTop(balloon1)
            }
        }else{ // ?????????
            dlist.clear()
            binding.txtNoData.visibility = View.INVISIBLE
            proc = true
            dlistCnt = 0
            dnameCnt = 0
        }

        for (element in nameListD) { //IName?????? DList ID ??????
            Log.d("FireStore","refrigerator : ${element}")
            val searchList = StringFuncs().makeSearch(element)
            App.db.collection("IName")
                .whereIn("name", searchList)
                .get().addOnSuccessListener { result ->
                    for (document in result) {
                        seqList.add("${document["SEQ"]}")    //ID list
                        Log.d("FireStore","IName ${document["SEQ"]}")
                    }
                }.addOnFailureListener {
                    Log.d("FireStore", it.stackTraceToString())
                }.addOnCompleteListener {
                    dlistCnt ++
                    if (dlistCnt  == nameListD.size ) { //ID list ?????? ??????
                        val seqListD = seqList.distinct().sortedDescending()
                        if (seqListD.isNotEmpty()){
                            for (i in seqListD.indices){
                                App.db.collection("IList").document(seqListD[i])
                                    .get().addOnSuccessListener {  data ->
                                        if ("${data["essential"]}".toBoolean()){
                                            idList.add("${data["id"]}")
                                            countList.add("${data["id"]}")
                                        }
                                    }.addOnCompleteListener{
                                        seqCnt ++
                                        if (seqCnt == seqListD.size){
                                            val idListD = idList.distinct().sortedDescending() //Id List ?????? ?????? ??????
                                            Log.d("FireStore","IList" + "idListD : ${idListD.size}")
                                            if (idListD.isNotEmpty()) { //Id list??? ?????? ??????
                                                for (i in idListD.indices) {
                                                    App.db.collection("DList").document(idListD[i])
                                                        .get().addOnSuccessListener { document ->
                                                            val item = DList(   //DList Row
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
                                                                "",
                                                                2
                                                            )

                                                            if (item.date <= date   // ?????? ??? ?????? ?????? ????????????
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

                                                                // ?????????
                                                                App.db.collection("DName")
                                                                    .whereEqualTo("id", idListD[i])
                                                                    .whereEqualTo("code", langCode)
                                                                    .limit(1)
                                                                    .get()
                                                                    .addOnSuccessListener { result ->
                                                                        for (document in result) {
                                                                            val index =
                                                                                dlist.indexOfFirst { it.id == document["id"] as String }
                                                                            dlist[index].name =
                                                                                document["name"] as String
                                                                        }
                                                                        //Dname ??????(???????????? ??????, ????????? ??????)
                                                                    }.addOnFailureListener {
                                                                        Log.d("FireStore", "Fail")
                                                                    }.addOnCompleteListener {
                                                                        dnameCnt++
                                                                        if (dnameCnt == idListD.size) {
                                                                            for (item in dlist){
                                                                                item.flag = Collections.frequency(countList, item.id)
                                                                            }
                                                                            //????????? ?????? ??? ??????????????? ???
                                                                            val sortedList = dlist.distinctBy { it.id }
                                                                                .sortedByDescending { it.date }
                                                                                .sortedByDescending { it.id }
                                                                                .sortedByDescending { it.flag }
                                                                            dlist.clear()
                                                                            dlist.addAll(sortedList)
                                                                            for (i in 1 until dlist.size){
                                                                                if (i < 4){
                                                                                    dlist[i].flag = 2
                                                                                }else{
                                                                                    break
                                                                                }
                                                                            }
                                                                            binding.pbLoading.visibility =
                                                                                View.INVISIBLE
                                                                            adapter.notifyDataSetChanged()
                                                                            //????????????????????? ??????
                                                                            proc = false
                                                                            if (dlist.isEmpty()){
                                                                                binding.txtNoData.visibility = View.VISIBLE
                                                                            }else{
                                                                                binding.txtNoData.visibility = View.INVISIBLE
                                                                            }
                                                                        }
//                                                                        ???????????? ???????????? (???????????? ????????? ????????? ??????)
                                                                        adapter.notifyDataSetChanged()
                                                                    }
                                                            }else{
                                                                dnameCnt++
                                                                if (dnameCnt == idListD.size) {
                                                                    //????????? ????????? ??????????????? ??????
                                                                    for (item in dlist){
                                                                        item.flag = Collections.frequency(countList, item.id)
                                                                    }
                                                                    val sortedList = dlist.distinctBy { it.id }
                                                                        .sortedByDescending { it.date }
                                                                        .sortedByDescending { it.id }
                                                                        .sortedByDescending { it.flag }
                                                                    dlist.clear()
                                                                    dlist.addAll(sortedList)
                                                                    for (i in 1 until dlist.size){
                                                                        if (i < 4){
                                                                            dlist[i].flag = 2
                                                                        }else{
                                                                            break
                                                                        }
                                                                    }
                                                                    binding.pbLoading.visibility =
                                                                        View.INVISIBLE
                                                                    //?????? ?????? ????????? ??? ??????
                                                                    adapter.notifyDataSetChanged()
                                                                    // ???????????? ????????? ??????
                                                                    proc = false
                                                                    if (dlist.isEmpty()){
                                                                        binding.txtNoData.visibility = View.VISIBLE
                                                                    }else{
                                                                        binding.txtNoData.visibility = View.INVISIBLE
                                                                    }
                                                                }
                                                            }

                                                        }.addOnFailureListener {
                                                            dnameCnt++
                                                            if (dlistCnt == idListD.size) {
                                                                for (item in dlist){
                                                                    item.flag = Collections.frequency(countList, item.id)
                                                                }
                                                                val sortedList = dlist.distinctBy { it.id }
                                                                    .sortedByDescending { it.date }
                                                                    .sortedByDescending { it.id }
                                                                    .sortedByDescending { it.flag }
                                                                dlist.clear()
                                                                dlist.addAll(sortedList)
                                                                for (i in 1 until dlist.size){
                                                                    if (i < 4){
                                                                        dlist[i].flag = 2
                                                                    }else{
                                                                        break
                                                                    }
                                                                }

                                                                binding.pbLoading.visibility =
                                                                    View.INVISIBLE
                                                                adapter.notifyDataSetChanged()
                                                                proc = false
                                                                if (dlist.isEmpty()){
                                                                    binding.txtNoData.visibility = View.VISIBLE
                                                                }else{
                                                                    binding.txtNoData.visibility = View.INVISIBLE
                                                                }
                                                            }
                                                        }.addOnCompleteListener {
                                                            adapter.notifyDataSetChanged()
                                                        }
                                                }
                                            } else {
                                                for (item in dlist) {
                                                    item.flag =
                                                        Collections.frequency(countList, item.id)
                                                }
                                                val sortedList = dlist.distinctBy { it.id }
                                                    .sortedByDescending { it.date }
                                                    .sortedByDescending { it.id }
                                                    .sortedByDescending { it.flag }
                                                dlist.clear()
                                                dlist.addAll(sortedList)
                                                for (i in 1 until dlist.size){
                                                    if (i < 4){
                                                        dlist[i].flag = 2
                                                    }else{
                                                        break
                                                    }
                                                }
                                                adapter.notifyDataSetChanged()
                                                binding.pbLoading.visibility = View.INVISIBLE
                                                proc = false
                                                if (dlist.isEmpty()){
                                                    binding.txtNoData.visibility = View.VISIBLE
                                                }else{
                                                    binding.txtNoData.visibility = View.INVISIBLE
                                                }
                                            }
                                        }
                                        }
                            }

                        }
                        }
                    }

            }
    }

    // ?????? ?????? ??????????????? ??????
    private fun getTooltoInt(boolean: Boolean): Int {
        return if (boolean) {
            2
        } else {
            1
        }
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