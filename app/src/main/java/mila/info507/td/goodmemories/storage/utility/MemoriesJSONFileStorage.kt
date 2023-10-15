package mila.info507.td.goodmemories.storage.utility

import android.content.Context
import mila.info507.td.goodmemories.model.Memories
import org.json.JSONObject
import td.info507.mykount.storage.utility.file.JSONFileStorage

class MemoriesJSONFileStorage(context: Context) : JSONFileStorage<Memories>(context, "memories") {
    override fun create(id: Int, obj: Memories): Memories {
        return Memories(id, obj.title, obj.photo, obj.emotion, obj.date, obj.description)
    }

    override fun objectToJson(id: Int, obj: Memories): JSONObject {
        val json= JSONObject()
        //on ajoute dans le jsonobject toutes les infos qu'on a de base, et on utilise les static
        json.put(Memories.ID, id)
        json.put(Memories.TITLE, obj.title)
        json.put(Memories.PHOTO, obj.photo)
        json.put(Memories.EMOTION, obj.emotion)
        json.put(Memories.DATE, obj.date)
        json.put(Memories.DESCRIPTION, obj.description)

        return json
    }

    override fun jsonToObject(json: JSONObject): Memories {
        return Memories(
            json.getInt(Memories.ID),
            json.getString(Memories.TITLE),
            json.getString(Memories.PHOTO),
            json.getInt(Memories.EMOTION),
            json.getString(Memories.DATE),
            json.getString(Memories.DESCRIPTION)
        )
    }
}