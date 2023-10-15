package mila.info507.td.goodmemories.storage.utility

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import mila.info507.td.goodmemories.activity.DataBaseStorage
import mila.info507.td.goodmemories.helper.DataBaseHelper
import mila.info507.td.goodmemories.model.Memories

class MemoriesDataBaseStorage (context: Context): DataBaseStorage<Memories>(DataBaseHelper(context), "good_memories") {
    // PAS BIEN CAR A CHAQUE FOIS ON DOIT LE RECRER DONC FAIRE UN SINGLETON
    companion object {
        const val ID = 0
        const val TITLE = 1
        const val PHOTO = 2
        const val EMOTION = 3
        const val DATE = 4
        const val DESCRIPTION = 5
    }
    override fun objectToValues(obj: Memories): ContentValues {
        val values= ContentValues()     //on creer du contenu, on sait pas ce que c'est
        // PAS LE ID
        values.put(Memories.TITLE, obj.title)
        values.put(Memories.PHOTO, obj.photo)
        values.put(Memories.EMOTION, obj.emotion)
        values.put(Memories.DATE, obj.date)
        values.put(Memories.DESCRIPTION, obj.description)

        return values
    }

    override fun cursorToObject(cursor: Cursor): Memories {
        return Memories(
            cursor.getInt(ID),
            cursor.getString(TITLE),
            cursor.getString(PHOTO),
            cursor.getInt(EMOTION),
            cursor.getString(DATE),
            cursor.getString(DESCRIPTION)
        )//...
    }
}