package com.ths.thethskitchen_git_ver2021072601

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.ths.thethskitchen_git_ver2021072601.databinding.DailogRefrigeratorAddBinding
import com.ths.thethskitchen_git_ver2021072601.databinding.ItemRefrigeratorBinding

//냉장고 리스트 어뎁터
class RefrigeratorAtapter: RecyclerView.Adapter<RefrigeratorAtapter.Holder>() {
    var helper: SQLiteDBHelper? = null
    var refrigeratorList = mutableListOf<RefrigeratorList>()
    private lateinit var dialogAdd : DailogRefrigeratorAddBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemRefrigeratorBinding.inflate(LayoutInflater.from(parent.context),
            parent,false)   //아이템 뷰바인딩
        dialogAdd = DailogRefrigeratorAddBinding.inflate(LayoutInflater.from(parent.context),
            parent,false)   //다이얼로그 뷰바인딩
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val setList = refrigeratorList[position]
        holder.setRefrigeratorList(setList)
        val layoutParam = holder.itemView.layoutParams
        layoutParam.height = 300
        holder.itemView.requestLayout()
        //아이템 클릭 --> 수정화면
        holder.itemView.setOnClickListener{ it ->
            val builder = AlertDialog.Builder(it.context)
//            builder.setTitle(it.context.getString(R.string.refrigerator_update_title))

//            오류 예방
            if (dialogAdd.root.parent != null){
                (dialogAdd.root.parent as ViewGroup).removeView(dialogAdd.root)
            }

            builder.setView(dialogAdd.root)     //다이얼로그View
            dialogAdd.txtRefrigeratorAddName.setText(setList.name)  //재료명
            dialogAdd.txtRefrigeratorAddDesc.setText(setList.desc)  //비고

            val alertDailog = builder.create()
//          저장 버튼
            dialogAdd.btnRefrigeratorAdd.setOnClickListener {
                if (dialogAdd.txtRefrigeratorAddName.text.toString() != ""  ) {
                    setList.name = dialogAdd.txtRefrigeratorAddName.text.toString() //입력된 재료명
                    setList.desc = dialogAdd.txtRefrigeratorAddDesc.text.toString() //입력된 비고
                    helper?.updateRefiregierator(setList)  //로컬 refrigerator update
                    Toast.makeText(it.context, it.context.getString(R.string.msg_save_data),
                        Toast.LENGTH_SHORT).show()
                    alertDailog.dismiss()   //다이얼로그 종료
                    refrigeratorList[position] = setList  //리스트 데이터 변경
                    notifyDataSetChanged()  //변경 적용
                }else{  //재료 미입력
                    Toast.makeText(it.context,it.context.getString(R.string.msg_add_NoName),
                        Toast.LENGTH_SHORT).show()
                }
            }
//          취소버튼
            dialogAdd.btnRefrigeratorCanc.setOnClickListener {
                alertDailog.dismiss()   //다이얼로그 종료
            }
            alertDailog.show()
        }
    }

    override fun getItemCount(): Int {
        return refrigeratorList.size
    }

    inner class Holder(private val bindng: ItemRefrigeratorBinding): RecyclerView.ViewHolder(bindng.root) {
        private var mRefrigeratorList: RefrigeratorList? = null

        init {
            // 리스트 삭제버튼
            bindng.btnRefrigeratorDel.setOnClickListener{
                helper?.deleteRefiregierator(mRefrigeratorList!!)
                refrigeratorList.remove(mRefrigeratorList)
                notifyDataSetChanged()
            }
        }
        //아이템 세팅
        fun setRefrigeratorList(refrigeratorList: RefrigeratorList) {
            this.mRefrigeratorList = refrigeratorList
            bindng.txtRefrigeratorName.text = refrigeratorList.name //재료명
            bindng.txtRefrigeratorDate.text = StringFuncs().makeDateString(refrigeratorList.date)
            this.mRefrigeratorList = refrigeratorList
        }
    }
}
