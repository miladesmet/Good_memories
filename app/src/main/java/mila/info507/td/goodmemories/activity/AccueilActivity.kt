package mila.info507.td.goodmemories.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import mila.info507.td.goodmemories.R
import mila.info507.td.goodmemories.adapter.MemoriesAdapter

class AccueilActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accueil)

            var list : RecyclerView = findViewById(R.id.memories_list)
        list.adapter=  MemoriesAdapter()
    }
}