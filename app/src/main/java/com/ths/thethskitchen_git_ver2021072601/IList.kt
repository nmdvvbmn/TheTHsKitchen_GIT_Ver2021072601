package com.ths.thethskitchen_git_ver2021072601

import java.io.Serializable

data class IList(
    var id: Long,
    var no: String,
    var name:String,
    var qunt: Float,
    var quntunit: String,
    var need: Boolean ): Serializable //
