package com.ths.a20210713_sqllitehelper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import androidx.core.os.persistableBundleOf

//import android.util.Log

class SqliteHelper(
    context: Context,
    name: String,
    version: Int
) : SQLiteOpenHelper(context, name, null, version) {
    //val context by lazy { context }
    override fun onCreate(db: SQLiteDatabase?) {
        createTableDlist(db)
        createTableIlist(db)

    }

    private fun createTableIlist(db: SQLiteDatabase?) {
        val iCreate = "CREATE TABLE ilist (" +
                "id text primary key, " +
                "no text, "+
                "name text, "+
                "qunt integer, "+
                "quntunit text, "+
                "need boolean "+
                ")"
        db?.execSQL(iCreate)
    }

    private fun createTableDlist(db: SQLiteDatabase?) {
        val dCreate = "CREATE TABLE dlist (" +
                "no text primary key, " +
                "name text, "+
                "pretime integer, "+
                "preunit text, "+
                "time integer, "+
                "timeunit text, "+
                "serve integer, "+
                "serveunit text, "+
                "tag text, "+
                "link text, "+
                "thumb text, "+
                "startin integer, "+
                "endin integer "+
                ")"
        db?.execSQL(dCreate)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

     fun insertDList(dlist: DList){
        val values = ContentValues()
        Log.d("SQL1","INSERT")
        values.put("no", dlist.no)
        values.put("name", dlist.name)
        values.put("pretime", dlist.pretime)
        values.put("preunit", dlist.preunit)
        values.put("time", dlist.time)
        values.put("timeunit", dlist.timeunit)
        values.put("serve", dlist.serve)
        values.put("serveunit", dlist.serveunit)
        values.put("tag", dlist.tag)
        values.put("link", dlist.link)
        values.put("thumb", dlist.thumb)
         values.put("startin", dlist.startin)
         values.put("endin", dlist.endin)
        val wd = writableDatabase
        wd.insert("dlist",null,values)
//        wd.close()
    }

    fun transDList(dlist: List<List<DList>>?) {
        if (dlist != null) {
            for (i in 0 until dlist.size) {
                var item = dlist[i]
                var data = DList("${item[0]}",
                    "${item[1]}",
                    "${item[2]}".toLong(),
                    "${item[3]}",
                    "${item[4]}".toLong(),
                    "${item[5]}",
                    "${item[6]}".toLong(),
                    "${item[7]}",
                    "${item[10]}",
                    "${item[8]}",
                    "${item[9]}",
                    "${item[11]}".toLong(),
                    "${item[12]}".toLong() )
                insertDList(data)
            }
        }
    }

    fun selectDlist(): MutableList<DList> {
        val dlist = mutableListOf<DList>()
        val select = "select * from dlist"
        val rd = readableDatabase

        val cursor = rd.rawQuery(select,null)
        while (cursor.moveToNext()){
            val no = cursor.getString(cursor. getColumnIndex("no"))
            val name = cursor.getString(cursor.getColumnIndex("name"))
            val pretime = cursor.getLong(cursor.getColumnIndex("pretime"))
            val preunit = cursor.getString(cursor.getColumnIndex("preunit"))
            val time = cursor.getLong(cursor.getColumnIndex("time"))
            val timeunit = cursor.getString(cursor.getColumnIndex("timeunit"))
            val serve = cursor.getLong(cursor.getColumnIndex("serve"))
            val serveunit = cursor.getString(cursor.getColumnIndex("serveunit"))
            val tag = cursor.getString(cursor.getColumnIndex("tag"))
            val link = cursor.getString(cursor.getColumnIndex("link"))
            val tumb = cursor.getString(cursor.getColumnIndex("thumb"))
            val startin = cursor.getLong(cursor.getColumnIndex("startin"))
            val endin = cursor.getLong(cursor.getColumnIndex("endin"))

            dlist.add(DList(no, name, pretime, preunit, time, timeunit, serve, serveunit, tag, link, tumb, startin, endin ))
        }
        cursor.close()
//        rd.close()
        return dlist
    }

    fun updateDlist(dlist: DList) {
        val values = ContentValues()
        values.put("name", dlist.name)
        values.put("pretime", dlist.pretime)
        values.put("preunit", dlist.preunit)
        values.put("time", dlist.time)
        values.put("timeunit", dlist.timeunit)
        values.put("serve", dlist.serve)
        values.put("serveunit", dlist.serveunit)
        values.put("tag", dlist.tag)
        values.put("link", dlist.link)
        values.put("thumb", dlist.thumb)
        values.put("startin", dlist.startin)
        values.put("endin", dlist.endin)
        val wd = writableDatabase
        wd.update("dlist",values, "no = ${dlist.no}", null)
//        wd.close()
    }

    fun deleteDlist(dlist: DList) {
       val delete = "delete from memo where no = ${dlist.no}"
       val db = writableDatabase
        Log.d("SQL1","DELETE")
       db.execSQL(delete)
//       db.close()
    }

    fun selectIRangeMin(): String {
        val rangeMin = "SELECT * FROM dlist ORDER BY startin DESC LIMIT 1"
        val rd = readableDatabase
        val ranges = rd.rawQuery(rangeMin,null)
        var range: String = "0"
        while (ranges.moveToNext()){
            range = ranges.getString(ranges.getColumnIndex("no"))
        }
        ranges.close()
//        rd.close()

        return range
    }

    fun transIList(iList: List<List<IList>>?) {
        if (iList != null) {
            for (i in 0 until iList.size) {
                var item = iList[i]
                var data = IList("${item[0]}".toLong(),
                    "${item[1]}",
                    "${item[2]}",
                    "${item[3]}".toFloat(),
                    "${item[4]}",
                    "${item[5]}".toBoolean() )
                insertIList(data)
            }
        }
    }

    fun insertIList(data: IList) {
        val values = ContentValues()
        Log.d("SQL1","INSERT")
        values.put("id", data.id)
        values.put("no", data.no)
        values.put("name", data.name)
        values.put("qunt", data.qunt)
        values.put("quntunit", data.quntunit)
        values.put("need", data.need)
        val wd = writableDatabase
        wd.insert("ilist",null,values)
//        wd.close()
    }

    fun selectIlist(no: String): MutableList<IList> {
        val ilist = mutableListOf<IList>()
        val select = "select * from ilist where no = '${no}'"
        val rd = readableDatabase

        val cursor = rd.rawQuery(select,null)
        while (cursor.moveToNext()){
            val id = cursor.getLong(cursor. getColumnIndex("id"))
            val no = cursor.getString(cursor.getColumnIndex("no"))
            val name = cursor.getString(cursor.getColumnIndex("name"))
            val qunt = cursor.getFloat(cursor.getColumnIndex("qunt"))
            val quntunit = cursor.getString(cursor.getColumnIndex("quntunit"))
            val need = cursor.getInt(cursor.getColumnIndex("need"))

            ilist.add(IList(id, no, name, qunt, quntunit, if (need > 0) { true } else{ false }))
        }
        cursor.close()
//        rd.close()
        return ilist
    }
}
