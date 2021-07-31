package com.ths.thethskitchen_git_ver2021072601

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.time.LocalDateTime

class CartHelper (
    context: Context,
    name: String,
    version: Int
) : SQLiteOpenHelper(context, name, null, version) {
    override fun onCreate(db: SQLiteDatabase?) {
        createTable(db)
    }

    private fun createTable(db: SQLiteDatabase?) {
        val create = "CREATE TABLE IF NOT EXISTS cart (" +
                "id integer primary key autoincrement," +
                "name text," +
                "desc text," +
                "date text )"
        db?.execSQL(create)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    fun select() : MutableList<CartList> {
        val cartList = mutableListOf<CartList>()
        val select = "select * from cart"
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

    fun delete(cartList: CartList) {
        val delete = "delete from cart where id = ${cartList.id}"
        val db = writableDatabase
        db.execSQL(delete)
    }

    fun update(cartList: CartList) {
        val values = ContentValues()
        val wd = writableDatabase
        values.put("id", cartList.id)
        values.put("name", cartList.name)
        values.put("desc", cartList.desc)
        values.put("date", cartList.date.toString())
        wd.update("cart", values, "id = ${cartList.id}", null)
    }

    fun insert(cartList: CartList) {
        val values = ContentValues()
        val wd = writableDatabase
        values.put("name", cartList .name)
        values.put("desc", cartList.desc)
        values.put("date", cartList.date.toString())
        wd.insert("cart",null, values)
    }

    fun move(cartList: CartList) {
        val values = ContentValues()
        val wd = writableDatabase
        values.put("name", cartList.name)

        cartList.desc = cartList.desc + "\n" + StringFuncs().makeDateString(cartList.date)
        values.put("desc", cartList.desc)

        values.put("date", LocalDateTime.now().toString())
        wd.insert("refrigerator",null, values)
        delete(cartList)

    }
}
