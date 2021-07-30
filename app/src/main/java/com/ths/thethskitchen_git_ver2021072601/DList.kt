package com.ths.thethskitchen_git_ver2021072601

import java.io.Serializable

data class DList(
    var id: String,
    var name: String?,
    var date: Long,
    var pretime: Long,
    var preunit: String,
    var time: Long,
    var timeunit: String,
    var qunt: Long,
    var quntunit: String,
    var start: Long,
    var stove: Long,
    var oven: Long,
    var micro: Long,
    var belnder: Long,
    var airfryer: Long,
    var multi: Long,
    var steamer: Long,
    var sousvide: Long,
    var grill: Long,
    var video: String ): Serializable

