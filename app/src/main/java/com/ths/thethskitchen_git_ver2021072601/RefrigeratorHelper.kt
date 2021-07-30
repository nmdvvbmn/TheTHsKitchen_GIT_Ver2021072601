package com.ths.thethskitchen_git_ver2021072601

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.time.LocalDateTime

//냉장고 DB SQLite
class RefrigeratorHelper(
    context: Context,
    name: String,
    version: Int
) : SQLiteOpenHelper(context, name, null, version) {
    override fun onCreate(db: SQLiteDatabase?) {
        createTable(db)
    }

    private fun createTable(db: SQLiteDatabase?) {
        val create = "CREATE TABLE if not exists refrigerator (" +
                "id integer primary key autoincrement," +
                "name text," +
                "desc text," +
                "date text )"
        db?.execSQL(create)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    fun select() : MutableList<RefrigeratorList> {
        val refrigeratorList = mutableListOf<RefrigeratorList>()
        val select = "select * from refrigerator"
        val rd = readableDatabase
        val cursor = rd.rawQuery(select,null)
        while (cursor.moveToNext()){
            val id = cursor.getLong(cursor.getColumnIndex("id"))
            val name = cursor.getString(cursor.getColumnIndex("name"))
            val desc = cursor.getString(cursor.getColumnIndex("desc"))
            val date = LocalDateTime.parse(cursor.getString(
                (cursor.getColumnIndex("date"))))

            refrigeratorList.add(RefrigeratorList(id,name,desc,date))
        }
        cursor.close()
        return refrigeratorList
    }

    fun delete(refrigeratorList: RefrigeratorList) {
        val delete = "delete from refrigerator where id = ${refrigeratorList.id}"
        val db = writableDatabase
        db.execSQL(delete)
    }

    fun update(refrigeratorList: RefrigeratorList) {
        val values = ContentValues()
        val wd = writableDatabase
        values.put("id", refrigeratorList.id)
        values.put("name", refrigeratorList.name)
        values.put("desc", refrigeratorList.desc)
        values.put("date", refrigeratorList.date.toString())
        wd.update("refrigerator", values, "id = ${refrigeratorList.id}", null)
        }

    fun insert(refrigeratorList: RefrigeratorList) {
        val values = ContentValues()
        val wd = writableDatabase
        values.put("name", refrigeratorList.name)
        values.put("desc", refrigeratorList.desc)
        values.put("date", refrigeratorList.date.toString())
        wd.insert("refrigerator",null, values)
    }
}
