package com.ths.thethskitchen_git_ver2021072601

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.time.LocalDateTime

class SQLiteDBHelper (
    context: Context,
    name: String,
    version: Int
) : SQLiteOpenHelper(context, name, null, version) {
    override fun onCreate(db: SQLiteDatabase?) {
        createTable(db)
    }
    private fun createTable(db: SQLiteDatabase?) {
        var create = "CREATE TABLE if not exists refrigerator (" +
                "id integer primary key autoincrement, " +
                "name text, " +
                "desc text, " +
                "date text )"
        db?.execSQL(create)

        create = "CREATE TABLE IF NOT EXISTS cart (" +
                "id integer primary key autoincrement, " +
                "name text, " +
                "desc text, " +
                "date text )"
        db?.execSQL(create)

        create = "CREATE TABLE IF NOT EXISTS favorites (" +
                "id text, " +
                "name text, " +
                "date integer, " +
                "pretime integer, " +
                "preunit text, " +
                "time integer, " +
                "timeunit text, " +
                "qunt integer, " +
                "quntunit text, " +
                "start integer, " +
                "stove integer, " +
                "oven integer, " +
                "micro integer, " +
                "blender integer, " +
                "airfryer integer, " +
                "multi integer, " +
                "steamer integer, " +
                "sousvide integer, " +
                "grill integer, " +
                "video text, " +
                "desc text )"
        db?.execSQL(create)
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    fun select_refrigerator() : MutableList<RefrigeratorList> {
        val refrigeratorList = mutableListOf<RefrigeratorList>()
        val select = "select * from refrigerator ORDER BY date DESC"
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

    fun delete_refiregierator(refrigeratorList: RefrigeratorList) {
        val delete = "delete from refrigerator where id = ${refrigeratorList.id}"
        val db = writableDatabase
        db.execSQL(delete)
    }

    fun update_refiregierator(refrigeratorList: RefrigeratorList) {
        val values = ContentValues()
        val wd = writableDatabase
        values.put("id", refrigeratorList.id)
        values.put("name", refrigeratorList.name)
        values.put("desc", refrigeratorList.desc)
        values.put("date", refrigeratorList.date.toString())
        wd.update("refrigerator", values, "id = ${refrigeratorList.id}", null)
    }

    fun insert_refiregierator(refrigeratorList: RefrigeratorList) {
        val values = ContentValues()
        val wd = writableDatabase
        values.put("name", refrigeratorList.name)
        values.put("desc", refrigeratorList.desc)
        values.put("date", refrigeratorList.date.toString())
        wd.insert("refrigerator",null, values)
    }

    fun select_cart() : MutableList<CartList> {
        val cartList = mutableListOf<CartList>()
        val select = "select * from cart ORDER BY date DESC"
        val rd = readableDatabase
        val cursor = rd.rawQuery(select,null)
        while (cursor.moveToNext()){
            val id = cursor.getLong(cursor.getColumnIndex("id"))
            val name = cursor.getString(cursor.getColumnIndex("name"))
            val desc = cursor.getString(cursor.getColumnIndex("desc"))
            val date = LocalDateTime.parse(cursor.getString(
                (cursor.getColumnIndex("date"))))

            cartList.add(CartList(id,name,desc,date))
        }
        cursor.close()
        return cartList
    }

    fun delete_cart(cartList: CartList) {
        val delete = "delete from cart where id = ${cartList.id}"
        val db = writableDatabase
        db.execSQL(delete)
    }

    fun update_cart(cartList: CartList) {
        val values = ContentValues()
        val wd = writableDatabase
        values.put("id", cartList.id)
        values.put("name", cartList.name)
        values.put("desc", cartList.desc)
        values.put("date", cartList.date.toString())
        wd.update("cart", values, "id = ${cartList.id}", null)
    }

    fun insert_cart(cartList: CartList) {
        val values = ContentValues()
        val wd = writableDatabase
        values.put("name", cartList .name)
        values.put("desc", cartList.desc)
        values.put("date", cartList.date.toString())
        wd.insert("cart",null, values)
    }

    fun move_cart(cartList: CartList) {
        val values = ContentValues()
        val wd = writableDatabase

        cartList.desc = cartList.desc + "\n" + StringFuncs().makeDateString(cartList.date)

        var refrigeratorList = RefrigeratorList( 0, cartList.name, cartList.desc , LocalDateTime.now() )
        insert_refiregierator(refrigeratorList)

        delete_cart(cartList)

    }

    fun select_favorites() : MutableList<DList> {
        val dList = mutableListOf<DList>()
        val select = "select * from favorites ORDER BY date DESC"
        val rd = readableDatabase
        val cursor = rd.rawQuery(select,null)
        while (cursor.moveToNext()){
            val id = cursor.getString(cursor.getColumnIndex("id"))
            val name = cursor.getString(cursor.getColumnIndex("name"))
            val date = cursor.getLong(cursor.getColumnIndex("date"))
            val pretime = cursor.getLong(cursor.getColumnIndex("pretime"))
            val preunit = cursor.getString(cursor.getColumnIndex("preunit"))
            val time = cursor.getLong(cursor.getColumnIndex("time"))
            val timeunit = cursor.getString(cursor.getColumnIndex("timeunit"))
            val qunt = cursor.getLong(cursor.getColumnIndex("qunt"))
            val quntunit = cursor.getString(cursor.getColumnIndex("quntunit"))
            val start = cursor.getLong(cursor.getColumnIndex("start"))
            val stove = cursor.getLong(cursor.getColumnIndex("stove"))
            val oven = cursor.getLong(cursor.getColumnIndex("oven"))
            val micro = cursor.getLong(cursor.getColumnIndex("micro"))
            val blender = cursor.getLong(cursor.getColumnIndex("blender"))
            val airfryer = cursor.getLong(cursor.getColumnIndex("airfryer"))
            val multi = cursor.getLong(cursor.getColumnIndex("multi"))
            val steamer = cursor.getLong(cursor.getColumnIndex("steamer"))
            val sousvide = cursor.getLong(cursor.getColumnIndex("sousvide"))
            val grill = cursor.getLong(cursor.getColumnIndex("grill"))
            val video = cursor.getString(cursor.getColumnIndex("video"))
            val desc = cursor.getString(cursor.getColumnIndex("desc"))

            dList.add(DList(id,name,date, pretime, preunit, time, timeunit, qunt, quntunit, start,
                stove, oven, micro, blender, airfryer, multi, steamer, sousvide, grill, video, desc))
        }
        cursor.close()
        return dList
    }

    fun delete_favorites(dList: DList) {
        val delete = "delete from favorites where id = '${dList.id}'"
        val db = writableDatabase
        db.execSQL(delete)
    }

    fun insert_favorites(dList: DList) {
        val values = ContentValues()
        val wd = writableDatabase
        values.put("id", dList .id)
        values.put("name", dList .name)
        values.put("date", dList .date)
        values.put("pretime", dList .pretime)
        values.put("preunit", dList .preunit)
        values.put("time", dList .time)
        values.put("timeunit", dList .timeunit)
        values.put("qunt", dList .qunt)
        values.put("quntunit", dList .quntunit)
        values.put("start", dList .start)
        values.put("stove", dList .stove)
        values.put("oven", dList .oven)
        values.put("micro", dList .micro)
        values.put("blender", dList .blender)
        values.put("airfryer", dList .airfryer)
        values.put("multi", dList .multi)
        values.put("steamer", dList .steamer)
        values.put("sousvide", dList .sousvide)
        values.put("grill", dList .grill)
        values.put("video", dList .video)

        wd.insert("favorites",null, values)
    }

    fun exists_favorites(id: String) : Boolean {
        val select = "select exists ( " +
                "select id from favorites where id =  '${id}' LIMIT 1)"
        val rd = readableDatabase
        val cursor = rd.rawQuery(select, null)
        cursor.moveToFirst()
        return if(cursor?.getInt(0) == 1){
            cursor?.close()
            true
        } else {
            cursor?.close()
            false
        }
    }
}
