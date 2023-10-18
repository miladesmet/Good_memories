package mila.info507.td.goodmemories.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import mila.info507.td.goodmemories.R
import mila.info507.td.goodmemories.adapter.Emotion
import mila.info507.td.goodmemories.adapter.EmotionsAdapter
import mila.info507.td.goodmemories.adapter.MemoriesAdapter
import mila.info507.td.goodmemories.model.Memories
import mila.info507.td.goodmemories.request.RequestEmotions
import mila.info507.td.goodmemories.storage.MemoriesStorage
import org.json.JSONArray
import java.io.File

class AccueilActivity : AppCompatActivity() {
    private lateinit var list_emotions : List<Emotion>
    private lateinit var list : RecyclerView
    private lateinit var adapter: MemoriesAdapter
    private lateinit var adapter_emotion: EmotionsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accueil)

        val rem = RequestEmotions(applicationContext)
        rem.getEmotions { response -> create_emotion_list(response) }

        MemoriesStorage.get(applicationContext).insert(Memories(0, "Voyage en Espagne", "photo.png", 1, "102/10/2023", "1 c'était drôle"))
        MemoriesStorage.get(applicationContext).insert(Memories(0, "Câlin d'amoureuses", "photo.png", 2, "100/10/2023", "2 je l'aime"))

        //création du RecyclerView avec tous les memories
        list = findViewById(R.id.memories_list)
        adapter= MemoriesAdapter(MemoriesStorage.get(applicationContext).findAll())
        list.adapter=  adapter
        adapter= MemoriesAdapter(MemoriesStorage.get(applicationContext).findAll())
        list.adapter=  adapter
        adapter.setOnItemClickListener(object : MemoriesAdapter.OnItemClickListener{
            override fun OnItemClick(position: Int) {
                val intent =Intent(this@AccueilActivity, MemorieActivity::class.java)
                intent.putExtra("position", MemoriesStorage.get(applicationContext).findAll()[position].id)
                startActivity(intent)
            }

        })

        //---------------------
        // Mise en place bouton tous
        //---------------------
        findViewById<TextView>(R.id.Tous).setOnClickListener {
            MemoriesStorage.get(applicationContext).insert(Memories(1, "Câlin", "photo.png", 1, "10/10/2023", "1 je sais pas"))
            loadAllMemories()
        }

        //---------------------
        // Mise en place bouton catégories
        //---------------------
        findViewById<TextView>(R.id.Categorie).setOnClickListener {
            loadAllEmotion()
        }

        //---------------------
        // Création du dossier images
        //---------------------
        val directory = File(filesDir, "images")
        if (!directory.exists()) {
            directory.mkdirs()
        }

        //---------------------
        // Bouton ajout de mémorie
        //---------------------
        val add_memorie: View = findViewById(R.id.add_memorie)
        add_memorie.setOnClickListener {
            val intent =Intent(this@AccueilActivity, AddMemorieActivity::class.java)
            startActivity(intent)
        }

    }

    private fun loadAllMemories() {
        adapter= MemoriesAdapter(MemoriesStorage.get(applicationContext).findAll())
        list.adapter=  adapter
        adapter.setOnItemClickListener(object : MemoriesAdapter.OnItemClickListener{
            override fun OnItemClick(position: Int) {
                val intent =Intent(this@AccueilActivity, MemorieActivity::class.java)
                intent.putExtra("position", MemoriesStorage.get(applicationContext).findAll()[position].id)
                startActivity(intent)
            }

        })
    }

    private fun loadMemoriesByEmotion(id: Int) {
        adapter = MemoriesAdapter(MemoriesStorage.get(applicationContext).findAllByEmotion(id))
        list.adapter=  adapter
        adapter.setOnItemClickListener(object : MemoriesAdapter.OnItemClickListener{
            override fun OnItemClick(position: Int) {
                val intent =Intent(this@AccueilActivity, MemorieActivity::class.java)
                println("MemoriesStorage.get(applicationContext).findAllByEmotion(id)[position].id")
                println(MemoriesStorage.get(applicationContext).findAllByEmotion(id)[position].id)
                intent.putExtra("position", MemoriesStorage.get(applicationContext).findAllByEmotion(id)[position].id)
                startActivity(intent)
            }

        })
    }

    private fun loadAllEmotion() {

        adapter_emotion = EmotionsAdapter(list_emotions)
        list.adapter=  adapter_emotion
        adapter_emotion.setOnItemClickListener(object : EmotionsAdapter.OnItemClickListener{
            override fun OnItemClick(position: Int) {
                println("CACA________________")
                loadMemoriesByEmotion(list_emotions[position].id)
            }

        })
        println("bababa__________________________")
    }

    fun create_emotion_list(emotions: JSONArray){
        val emotions_list : ArrayList<Emotion> = ArrayList()


        for (i in 0 until emotions.length()) {
            val emotion = emotions.getJSONObject(i)

            emotions_list.add(Emotion(emotion.getInt("id"), emotion.getString("image_url"), emotion.getString("title")))

        }
        println("emotions_list.toList()--------------------------------------")
        println(emotions.toString())
        if (emotions.length()!=0) {
            list_emotions = emotions_list.toList()
        }
    }
}