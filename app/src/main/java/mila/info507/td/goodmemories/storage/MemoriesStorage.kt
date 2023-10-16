package mila.info507.td.goodmemories.storage

import android.content.Context
import android.content.SharedPreferences
import mila.info507.td.goodmemories.model.Memories
import mila.info507.td.goodmemories.storage.utility.MemoriesJSONFileStorage

object MemoriesStorage {

    private var jsonFileStorage: MemoriesJSONFileStorage? = null

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("mila.info507.td.goodmemories.preferences", Context.MODE_PRIVATE)
    }

    fun get(context: Context): Storage<Memories> {
        if (jsonFileStorage == null) {
            jsonFileStorage = MemoriesJSONFileStorage(context)
        }
        return jsonFileStorage!!
    }
}