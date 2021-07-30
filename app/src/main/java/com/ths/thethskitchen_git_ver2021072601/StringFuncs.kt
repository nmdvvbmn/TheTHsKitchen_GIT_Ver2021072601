package com.ths.thethskitchen_git_ver2021072601

import android.content.Context
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

open class StringFuncs {

    open fun makeDesc( data: DList, context: Context ): String {
       var output: String = ""

        output = context.getString(R.string.ctime) + " : " + data.time.toString()
        when (data.timeunit) {
            "분" -> output = output + context.getString(R.string.min) + "\n"
            "시간" -> output = output + context.getString(R.string.hour) + "\n"
            "일" -> output = output + context.getString(R.string.day) + "\n"
            "주" -> output = output + context.getString(R.string.week) + "\n"
            else -> output = output + "\n"
        }
        output = output + context.getString(R.string.ptime) + " : " + data.pretime.toString()
        when (data.preunit) {
            "분" -> output = output + context.getString(R.string.min) + "\n"
            "시간" -> output = output + context.getString(R.string.hour) + "\n"
            "일" -> output = output + context.getString(R.string.day) + "\n"
            "주" -> output = output + context.getString(R.string.week) + "\n"
            else -> output = output + "\n"
        }

        if (data.stove > 0 || data.oven > 0 ||  data.micro > 0 ||  data.belnder > 0 ||
            data.airfryer > 0 || data.multi > 0 ||  data.steamer > 0 ||  data.sousvide > 0 ||
            data.grill > 0 )

        output = output + context.getString(R.string.tool) + "\n"

        if(data.stove > 0) {
            output = output + context.getString(R.string.stove) + "  "
        }
        if(data.oven > 0) {
            output = output + context.getString(R.string.oven) + "  "
        }
        if(data.micro > 0) {
            output = output + context.getString(R.string.micro) + "  "
        }
        if(data.belnder > 0) {
            output = output + context.getString(R.string.blender) + "  "
        }
        if(data.airfryer > 0) {
            output = output + context.getString(R.string.airfryer) + "  "
        }
        if(data.multi > 0) {
            output = output + context.getString(R.string.multi) + "  "
        }
        if(data.steamer > 0) {
            output = output + context.getString(R.string.steam) + "  "
        }
        if(data.sousvide > 0) {
            output = output + context.getString(R.string.sous) + "  "
        }
        if(data.grill > 0) {
            output = output + context.getString(R.string.grill)
        }

       return  output
    }
    open fun makeDateString( date: LocalDateTime ): String {
        return date.format(DateTimeFormatter.ISO_DATE_TIME)
    }
}