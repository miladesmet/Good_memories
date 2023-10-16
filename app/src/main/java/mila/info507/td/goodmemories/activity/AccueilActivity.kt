package mila.info507.td.goodmemories.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import mila.info507.td.goodmemories.R
import mila.info507.td.goodmemories.adapter.MemoriesAdapter
import mila.info507.td.goodmemories.model.Memories
import mila.info507.td.goodmemories.storage.MemoriesStorage
import java.io.File

class AccueilActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accueil)

        MemoriesStorage.get(applicationContext).insert(Memories(0, "Voyage en Espagne", "photo.png", 1, "12/10/2023", "c'était drôle"))
        MemoriesStorage.get(applicationContext).insert(Memories(1, "Câlin d'amoureuses", "photo.png", 2, "10/10/2023", "je l'aime"))

        //création du RecyclerView avec tous les memories
        val list : RecyclerView = findViewById(R.id.memories_list)
        var adapter= MemoriesAdapter(MemoriesStorage.get(applicationContext).findAll())
        list.adapter=  adapter
        adapter.setOnItemClickListener(object : MemoriesAdapter.OnItemClickListener{
            override fun OnItemClick(position: Int) {
                val intent =Intent(this@AccueilActivity, MemorieActivity::class.java)
                intent.putExtra("position", position)
                startActivity(intent)
            }

        })

        //---------------------
        // Mise en place bouton tous
        //---------------------
        findViewById<TextView>(R.id.Tous).setOnClickListener{
            println("Tous")
            MemoriesStorage.get(applicationContext).insert(Memories(1, "Câlin d'amoureuses", "photo.png", 3, "10/10/2023", "je l'aime"))

            adapter= MemoriesAdapter(MemoriesStorage.get(applicationContext).findAll())
            list.adapter=  adapter
            adapter.setOnItemClickListener(object : MemoriesAdapter.OnItemClickListener{
                override fun OnItemClick(position: Int) {
                    val intent =Intent(this@AccueilActivity, MemorieActivity::class.java)
                    intent.putExtra("position", position)
                    startActivity(intent)
                }

            })
        }

        //---------------------
        // Mise en place bouton catégories
        //---------------------
        findViewById<TextView>(R.id.Categorie).setOnClickListener{
            println("Catégorie")
            MemoriesStorage.get(applicationContext).insert(Memories(0, "Voyage en Espagne", "photo.png", 4, "12/10/2023", "c'était drôle"))

            adapter= MemoriesAdapter(MemoriesStorage.get(applicationContext).findAllByEmotion())
            list.adapter=  adapter
            adapter.setOnItemClickListener(object : MemoriesAdapter.OnItemClickListener{
                override fun OnItemClick(position: Int) {
                    val intent =Intent(this@AccueilActivity, MemorieActivity::class.java)
                    intent.putExtra("position", position)
                    startActivity(intent)
                }

            })
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
}