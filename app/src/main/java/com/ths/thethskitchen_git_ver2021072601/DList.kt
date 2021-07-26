package com.ths.a20210713_sqllitehelper

import java.io.Serializable

data class DList(
    var no: String,
    var name:String,
    var pretime: Long,
    var preunit: String,
    var time: Long,
    var timeunit: String,
    var serve: Long,
    var serveunit: String,
    var tag: String,
    var link: String,
    var thumb: String,
    var startin: Long,
    var endin: Long ): Serializable

