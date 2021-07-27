package com.ths.thethskitchen_git_ver2021072601

import java.io.Serializable

data class DList(
    var id: String,
//    var name:String,
    var date: Long,
    var pretime: Long,
    var preunit: String,
    var time: Long,
    var timeunit: String,
    var qunt: Long,
    var quntunit: String,
    var video: String ): Serializable

