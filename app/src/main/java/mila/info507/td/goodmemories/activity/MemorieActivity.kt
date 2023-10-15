package mila.info507.td.goodmemories.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import mila.info507.td.goodmemories.R
import mila.info507.td.goodmemories.model.Memories
import mila.info507.td.goodmemories.storage.MemoriesStorage

class MemorieActivity() : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memorie)

        //recupere les put extra
        val bundle: Bundle?= intent.extras
        val position = bundle!!.getInt("position")

        var memorie: Memories? = MemoriesStorage.get(applicationContext).find(position+1)

        val title: TextView = findViewById(R.id.title)
        val date: TextView = findViewById(R.id.date)
        val emotion: ImageView = findViewById(R.id.emotion)
        val photo: ImageView = findViewById(R.id.photo)
        val description: TextView = findViewById(R.id.description)

        if (memorie != null) {
            title.text = memorie.title
            date.text = memorie.date
            //emotion.setImageResource()
            //photo.setImageResource()
            description.text = memorie.description
        }

    }
}