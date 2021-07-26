package com.ths.a20210713_sqllitehelper

import java.io.Serializable

data class IList(
    var id: Long,
    var no: String,
    var name:String,
    var qunt: Float,
    var quntunit: String,
    var need: Boolean ): Serializable //
