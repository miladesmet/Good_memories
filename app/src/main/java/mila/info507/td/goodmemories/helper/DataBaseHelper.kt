package mila.info507.td.goodmemories.helper

import mila.info507.td.goodmemories.model.Memories
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class DataBaseHelper(context: Context): SQLiteOpenHelper(context, "good_memories.db", null, 1 ) { //pas de factory, la version c'est la premiere



    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE Memories (" +
                    "${BaseColumns._ID} INTEGER," +
                    "${Memories.TITLE} TEXT," +
                    "${Memories.PHOTO} TEXT,"+
                    "${Memories.EMOTION} INTEGER," +
                    "${Memories.DATE} DATE," +
                    "${Memories.DESCRIPTION} TEXT," +
                    "PRIMARY KEY(${BaseColumns._ID})" +
                    ")"
        )
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }
}