package mila.info507.td.goodmemories.activity

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import mila.info507.td.goodmemories.R
import mila.info507.td.goodmemories.model.Memories
import mila.info507.td.goodmemories.request.RequestEmotions
import mila.info507.td.goodmemories.storage.MemoriesStorage
import org.json.JSONArray
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ModifActivity : AppCompatActivity() {

    companion object {
        const val PICK_IMAGE_REQUEST = 1
    }

    // Pour récuperer le path de l'image ajoutée
    private var imagePath: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modif)

        // On recupere en extra l'id du memorie à modifier
        val bundle: Bundle?= intent.extras
        val id = bundle!!.getInt("idMemorie")

        // On recupere le memorie
        val memorie: Memories? = MemoriesStorage.get(applicationContext).find(id)

        // On récupère chaque element XML à remplir
        val title: EditText = findViewById(R.id.input_titre)
        val date: EditText = findViewById(R.id.input_date)
        val photo: ImageView = findViewById(R.id.ajouter_image)
        val description: EditText = findViewById(R.id.input_contenu)

        // Gestion date Picker
        gestion_datePicker()

        if (memorie != null) {

            // Récuperation de la liste des émotions pour le radio

            val rem : RequestEmotions = RequestEmotions(applicationContext)
            val emotions = rem.getEmotions({response -> create_emotion_radio_with_preselection(response, memorie.emotion)})

            // On leur attributs des valeurs de préremplissage

            title.setText(memorie.title)
            date.setText(memorie.date)
            description.setText(memorie.description)

            // init du chemin image
            imagePath =memorie.photo


            // Gestion photo
            val bouton_ajout_image =  findViewById<ImageView>(R.id.ajouter_image)

            println(imagePath.toString())
            if (imagePath != "") {
                Glide.with(this)
                    .load(imagePath)
                    .into(bouton_ajout_image)
            }

            bouton_ajout_image.setOnClickListener{
                pickImageFromGallery()
            }


            // Bouton annuler

            val bouton_annuler: TextView= findViewById(R.id.annuler)
            bouton_annuler
                .setOnClickListener{
                    finish()
                }




            // Bouton supprimer

            val bouton_supprimer: TextView= findViewById(R.id.supprimer_memorie)
            bouton_supprimer
                .setOnClickListener{
                    MemoriesStorage.get(applicationContext).delete(id)

                    // Je fais ça au lieu de finish pour éviter de retourner sur la page du memorie que je viens de supprimer
                    val intent = Intent(applicationContext, AccueilActivity::class.java)

                    // Toast pour informer l'utilisateur que tout c'est bien déroulé
                    Toast.makeText(applicationContext, "Le memorie a bien été supprimé", Toast.LENGTH_SHORT).show()

                    startActivity(intent)
                }




            // Bouton Enregistrer

            val bouton_enregistrer: TextView = findViewById(R.id.Enregistrer)
            bouton_enregistrer
                .setOnClickListener {

                    // On récupere les valeurs du formulaire
                    val newTitle: String = title.text.toString()
                    val newDate: String = date.text.toString()

                    val newDescription: String = description.text.toString()

                    // Validation du formulaire

                    var validation : Boolean = true
                    if (newTitle.isEmpty()){
                        Toast.makeText(applicationContext, "Veuillez remplir le titre", Toast.LENGTH_SHORT).show()
                        validation = false
                    }

                    if (newDate.isEmpty()){
                        Toast.makeText(applicationContext, "Veuillez remplir la date", Toast.LENGTH_SHORT).show()
                        validation = false
                    }

                    if (newDescription.isEmpty()){
                        Toast.makeText(applicationContext, "Veuillez remplir le contenu", Toast.LENGTH_SHORT).show()
                        validation = false
                    }
                    if (validation) {
                        // Nouveau memory
                        val updatedMemory: Memories = Memories(
                            id,
                            newTitle,
                            imagePath,
                            memorie.emotion,
                            newDate,
                            newDescription
                        )

                        // On met à jour le memorie
                        MemoriesStorage.get(applicationContext).update(id, updatedMemory)

                        // Toast pour informer l'utilisateur que tout c'est bien déroulé
                        Toast.makeText(
                            applicationContext,
                            "Le memorie a bien été mis à jour",
                            Toast.LENGTH_SHORT
                        ).show()

                        //On indique que tout c'est bien passé
                        val resultIntent = Intent()
                        resultIntent.putExtra("memoryId", id)
                        setResult(Activity.RESULT_OK, resultIntent)
                        finish()
                    }

                }
        }
    }


    fun create_emotion_radio_with_preselection(emotions: JSONArray, id_emotion: Int){
        val emotionRadioGroupContainer = findViewById<RadioGroup>(R.id.emotion_group)


        for (i in 0 until emotions.length()) {
            // Récupération de l'émotion suivante
            val emotion = emotions.getJSONObject(i)

            // Création du radio button
            val radioButton = RadioButton(this)
            radioButton.text = emotion.getString("title")
            radioButton.tag = emotion.getInt("id")

            // Ajout du radio button dans le RadioGroupContainer
            emotionRadioGroupContainer.addView(radioButton)
        }

        // Je l'appelle à la fin de cette fonction pour etre sur que ca a eu le temps de se crée car
        // risque de asynchrone dans la fonction principale
        preselectEmotion(id_emotion)

    }

    fun preselectEmotion(emotionIdToSelect: Int) {

        // On récupère le group radio
        val emotionRadioGroupContainer = findViewById<RadioGroup>(R.id.emotion_group)

        // On itere sur chaque enfant
        for (i in 0 until emotionRadioGroupContainer.childCount) {
            val radioButton = emotionRadioGroupContainer.getChildAt(i) as RadioButton
            // On récupère le tag du radio button courant
            val emotionId = radioButton.tag as Int
            // Si c'est celui qu'on cherche on le check
            if (emotionId == emotionIdToSelect) {
                radioButton.isChecked = true
                break
            }
        }
    }

    //-------------------------------
    // GESTION DE L'INPUT DATE
    //-------------------------------
    fun gestion_datePicker() {

        // code from : https://www.geeksforgeeks.org/how-to-popup-datepicker-while-clicking-on-edittext-in-android/

        val dateEdt: EditText = findViewById(R.id.input_date)

        dateEdt.setOnClickListener {
            val c = Calendar.getInstance()

            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(

                this,
                { view, year, monthOfYear, dayOfMonth ->

                    val dat = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                    dateEdt.setText(dat)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

    }


    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, ModifActivity.PICK_IMAGE_REQUEST)
    }

    //-------------------------------
    // GESTION DE L'IMPORTATION DE L'IMAGE
    //-------------------------------

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == AddMemorieActivity.PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            val imageUri: Uri? = data?.data

            if (imageUri != null) {

                // Récupération où créatio du dossier cible
                val directory = File(filesDir, "images")
                if (!directory.exists()) {
                    directory.mkdirs()
                }

                // création d'un nom unique
                val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                val fileName = "IMG_$timestamp.jpg"

                // Créez un fichier pour l'image
                val file = File(directory, fileName)


                //Copie le fichier image sélectionné dans le répertoire interne
                try {
                    val inputStream = contentResolver.openInputStream(imageUri) // imageUri est l'URI de l'image sélectionnée
                    val outputStream = FileOutputStream(file)

                    val buffer = ByteArray(1024)
                    var bytesRead: Int
                    while (true) {
                        val bytesRead = inputStream?.read(buffer) ?: -1
                        if (bytesRead == -1) {
                            break
                        }
                        outputStream.write(buffer, 0, bytesRead)
                    }

                    inputStream?.close()
                    outputStream.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                // Maintenant, 'file' contient le chemin de l'image dans le stockage interne
                imagePath = file.absolutePath

                // On recharge l'image
                val bouton_ajout_image =  findViewById<ImageView>(R.id.ajouter_image)

                Glide.with(this)
                    .load(imagePath)
                    .into(bouton_ajout_image)
            }
        }
    }


}