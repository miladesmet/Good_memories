package mila.info507.td.goodmemories.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import mila.info507.td.goodmemories.R
import mila.info507.td.goodmemories.model.Memories
import mila.info507.td.goodmemories.request.RequestEmotions
import mila.info507.td.goodmemories.storage.MemoriesStorage
import java.util.Calendar


class MemorieActivity() : AppCompatActivity() {

    fun ajouteImageEmotion(url: String){

        val emotion: ImageView = findViewById(R.id.emotion)
        Glide.with(applicationContext).load(url).into(emotion)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memorie)


    }

// On met ce code dan OnResume et pas on create pour que le "refresh" apres modif soit auto
    override fun onResume() {
        super.onResume()

        //recupere les put extra
        val bundle: Bundle?= intent.extras
        val position = bundle!!.getInt("position")

        //On récupère le mémorie
        val memorie: Memories? = MemoriesStorage.get(applicationContext).find(position+1)

        // On récupère chaque element XML à remplir
        val title: TextView = findViewById(R.id.title)
        val date: TextView = findViewById(R.id.date)
        val photo: ImageView = findViewById(R.id.photo)
        val description: TextView = findViewById(R.id.description)

        if (memorie != null) {
            title.text = memorie.title
            date.text = memorie.date
            //emotion.setImageResource()
            val reEm: RequestEmotions= RequestEmotions(applicationContext)
            reEm.getEmotionImageUrlById(memorie.emotion){ imageUrl ->  if (imageUrl != "") {
                ajouteImageEmotion(imageUrl)
            }}
            Glide.with(applicationContext).load(memorie.photo).into(photo)
            description.text = memorie.description
        }

        //Le bouton retour
        findViewById<ImageView>(R.id.retour)
            .setOnClickListener {
                finish()
            }

        // Le bouton modifier
        findViewById<TextView>(R.id.Modifier)
            .setOnClickListener{
                val intent = Intent(this, ModifActivity::class.java)

                intent.putExtra("idMemorie", position+1)

                startActivity(intent)
            }




    }



}