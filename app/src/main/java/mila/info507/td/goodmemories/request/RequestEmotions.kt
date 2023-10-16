package mila.info507.td.goodmemories.request

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class RequestEmotions(private val context: Context) {
    private val queue = Volley.newRequestQueue(context)
    private val emotionsUrl = "http://51.68.91.213/gr-1-1/emotions.json"

    fun getEmotionImageUrlById(emotionId: Int, callback: (String) -> Unit) {
        val emotionRequest = JsonObjectRequest(
            Request.Method.GET, emotionsUrl, null,
            { response ->
                try {
                    val emotionsArray = response.optJSONArray("emotions")
                    if (emotionsArray != null) {
                        for (i in 0 until emotionsArray.length()) {
                            val emotion = emotionsArray.getJSONObject(i)
                            val id = emotion.getInt("id")
                            val imageUrl = emotion.getString("image_url")
                            if (id == emotionId) {
                                callback(imageUrl)
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                callback("") // Émotion non trouvée
            },
            { error ->
                error.printStackTrace()
                callback("")
            }
        )
        queue.add(emotionRequest)
    }


    fun getEmotions(callback: (JSONArray) -> Unit) {
        val emotionRequest = JsonObjectRequest(
            Request.Method.GET, emotionsUrl, null,
            { response ->
                try {
                    val emotionsArray = response.optJSONArray("emotions")

                    if (emotionsArray != null) {
                        callback(emotionsArray)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                callback(JSONArray()) // Émotion non trouvée
            },
            { error ->
                error.printStackTrace()
                callback(JSONArray())
            }
        )
        queue.add(emotionRequest)
    }
}