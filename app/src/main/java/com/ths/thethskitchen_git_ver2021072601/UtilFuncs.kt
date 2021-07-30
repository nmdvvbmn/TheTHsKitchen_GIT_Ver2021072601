package com.ths.thethskitchen_git_ver2021072601

import java.util.*

open class UtilFuncs {

    open fun getLanguage(): String{
        var language = Locale.getDefault().getLanguage()
        if (language != null) {
            val codeList: List<String> = listOf("ko","en","zh-TW","zh-HK","gu","el","nl","ne","no",
                "da","de","lo","lv","la","ru","ro","lb","lt","mr","mk","ms","mn","eu","my","vi",
                "be","bn","bs","bg","sr","su","si","sw","sv","gd","es","es-419","es-es","sk","sl",
                "sd","ar","hy","is","ga","az","af","sq","am","eo","et","en-us","en-gb","or","ur",
                "uz","uk","cy","it","id","ja","jw","ka","zh-CN","cs","kk","ca","hr","km","ky","ta",
                "th","tr","ps","pa","fa","pt","pt-br","pt-pt","pl","hu","fr","fr-fr","fi","tl","he",
                "hi","GL","st","rw","mg","ml","yo","ig","yi","zu","kn","xh","ku","tg","tt","te",
                "tk","ha")
            if (codeList.any { it == language } ) {
                return language
            }else{
                return "en"
            }
        }else{
            return  "en"
        }
    }

}