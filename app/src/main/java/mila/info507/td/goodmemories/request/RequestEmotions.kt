package mila.info507.td.goodmemories.request

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray

class RequestEmotions(private val context: Context) {
    private val queue = Volley.newRequestQueue(context)
    private val emotionsUrl = "http://51.68.91.213/gr-1-1/emotions.json"

    fun getEmotionImageUrlById(emotionId: Int, callback: (String) -> Unit) {

        // On crée un Json Object Request pour faire un requette vers notre url et qu'elle nous renvoie le json contenant les émotions
        val emotionRequest = JsonObjectRequest(
            Request.Method.GET,
            emotionsUrl,
            null,
            // Fonction callback
            { response ->
                try {
                    val emotionsArray = response.optJSONArray("emotions")
                    if (emotionsArray != null) {
                        // On parcours les emotions jusqu'a trouvé celle qui a le bon id et la donner à la fonction callback
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
                // On a pas l'emotion avec l'id demandé
                callback("") // Émotion non trouvée
            },
            // On a pas réussi à obtenir le json
            { error ->
                error.printStackTrace()
                callback("")
            }
        )
        // On ajoute notre requete à la "queue" pour qu'elle soit lancée
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
                callback(JSONArray()) // Émotions non trouvées
            },
            // La requete c'est mal passée
            { error ->
                error.printStackTrace()
                callback(JSONArray())
            }
        )

        // On ajoute notre requete à la "queue" pour qu'elle soit lancée
        queue.add(emotionRequest)
    }
}