package com.ths.thethskitchen_git_ver2021072601

import java.time.LocalDateTime

data class RefrigeratorList(
    var id: Long,
    var name: String,
    var desc: String,
    var date: LocalDateTime
)