package com.ths.thethskitchen_git_ver2021072601

import java.io.Serializable
import java.time.LocalDateTime

//요리 List
data class DList(
    var id: String, //문서서번호
    var name: String?, //요리명
    var date: Long,     //업로드일자
    var pretime: Long,  //준비시간
    var preunit: String,    //준비시간단위
    var time: Long,     //요리시간
    var timeunit: String,   //요리시간단위
    var qunt: Long,     //양
    var quntunit: String,       //양단위
    var start: Long,        //영상시작시간
    var stove: Long,        //레인지/인덕션
    var oven: Long,         //오븐
    var micro: Long,        //전자레인지
    var belnder: Long,      //블랜더
    var airfryer: Long,     //에어프라이어
    var multi: Long,        //인스턴트팟
    var steamer: Long,      //찜기
    var sousvide: Long,     //수비드
    var grill: Long,        //그릴
    var video: String ): Serializable   //비디오ID

// 재료List
data class IList(
    var id: String, //요리ID
    var SEQ: String, //재료ID
    var name:String, //재료명
    var qunt: Long, //양  
    var quntunit: String,   //양단위
    var other: String,  //대채
    var desc: String,   //비고
    var need: Boolean ): Serializable //필수여부

//냉장고
data class RefrigeratorList(
    var id: Long,
    var name: String,
    var desc: String,
    var date: LocalDateTime
)

//장바구니
data class CartList(
    var id: Long,
    var name: String,
    var desc: String,
    var date: LocalDateTime
)