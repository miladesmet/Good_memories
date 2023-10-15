package td.info507.mykount.storage.utility.file

import android.content.Context
import org.json.JSONObject

abstract class JSONFileStorage<T>(context: Context, name: String) : FileStorage<T>(context, name, "json") {
    protected abstract fun objectToJson(id: Int, obj: T): JSONObject
    protected abstract fun jsonToObject(json: JSONObject): T

    override fun dataToString(data: HashMap<Int, T>): String {
        val json = JSONObject()
        data.forEach{pair -> json.put("${pair.key}", objectToJson(pair.key, pair.value))} //pair= couple (clé:element)
        return json.toString()
    }

    override fun stringToData(value: String): HashMap<Int, T> {
        val data = HashMap<Int, T>()
        val json= JSONObject(value)     //on transforme notre string en jsonobject
        val iterator= json.keys()       //iterateur dit s'il y a un suivant
        while (iterator.hasNext()){     //tant qu'il y a un suivant
            val key= iterator.next()        //on recupere le premier
            data.put(key.toInt(), jsonToObject(json.getJSONObject(key)))        // on met dans le json[key] les données à la key
        }
        return data
    }

}