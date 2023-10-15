package mila.info507.td.goodmemories.activity

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import mila.info507.td.goodmemories.storage.Storage

abstract class DataBaseStorage<T>(private val helper: SQLiteOpenHelper, private val table: String):
    Storage<T> {

    protected abstract fun objectToValues(obj: T): ContentValues
    protected abstract fun cursorToObject(cursor: Cursor): T

    override fun insert(obj: T): Int { //Flux d'écriture
        return helper.writableDatabase.insert(table, null, objectToValues(obj)).toInt()
    }

    override fun size(): Int { //Flux de lecture
        // on veux juste la taille de la table donc toutes les données sont vides
        return helper.readableDatabase.query(table, null, null, null, null, null,null,null).count
    }

    override fun find(id: Int): T? {
        var obj: T?= null;
        val cursor= helper.readableDatabase.query(table, //Flux de lecture
            null,"${BaseColumns._ID} = ?", arrayOf("$id"), null, null, null, null)
        //on a plusieurs lignes

        if(cursor.moveToNext()){
            obj= cursorToObject(cursor)
        }
        cursor.close()
        return obj;
    }

    override fun findAll(): List<T> {
        val list = arrayListOf<T>()
        val cursor = helper.readableDatabase.query(table, null, null, null, null, null, null)
        while (cursor.moveToNext()){
            list.add(cursorToObject((cursor)))
        }
        cursor.close()
        return list
    }

    override fun update(id: Int, obj: T) {
        helper.writableDatabase.update(
            table, objectToValues(obj), "${BaseColumns._ID} = ?", arrayOf("$id")
        )
    }

    override fun delete(id: Int) {
        helper.writableDatabase.delete(table, "${BaseColumns._ID}", arrayOf("$id"))
    }
}