package mila.info507.td.goodmemories.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import mila.info507.td.goodmemories.R

class CongratulationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // On relie l'activité au layout
        setContentView(R.layout.activity_congratulation)


        //------------
        // Mise en place du bouton annuler
        // -----------
        val retour_button : View = findViewById<TextView>(R.id.retour)
        retour_button.setOnClickListener{
            finish()
        }
        //------------
        // Mise en place du bouton ajouter un autre souvenir
        // -----------
        val ajouter_autre_bouton : View = findViewById<TextView>(R.id.button_ajouter)
        ajouter_autre_bouton.setOnClickListener{

            // Ouverture de l'activitée d'ajout
            val intent = Intent(this, AddMemorieActivity::class.java)
            startActivity(intent)

            // Fin de l'activité
            finish()
        }
    }
}