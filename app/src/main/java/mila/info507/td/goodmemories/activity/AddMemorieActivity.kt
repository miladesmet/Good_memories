package mila.info507.td.goodmemories.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import mila.info507.td.goodmemories.R
import mila.info507.td.goodmemories.model.Memories
import mila.info507.td.goodmemories.request.RequestEmotions
import mila.info507.td.goodmemories.storage.MemoriesStorage
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddMemorieActivity : AppCompatActivity() {


    // Pour récuperer le path de l'image ajoutée
    private var imagePath: String = ""

    companion object {
        const val PICK_IMAGE_REQUEST = 1
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_memorie)


        //PHOTO
        val bouton_ajout_image =  findViewById<ImageView>(R.id.ajouter_image)

        bouton_ajout_image.setOnClickListener{
            pickImageFromGallery()
        }


        // Récuperation de la liste des émotions pour le radio

        val rem : RequestEmotions = RequestEmotions(applicationContext)
        val emotions = rem.getEmotions({response -> create_emotion_radio(response)})


        gestion_datePicker()



            // On met en place le listener
            val enregistrer_button : View = findViewById<TextView>(R.id.Enregistrer)
            enregistrer_button.setOnClickListener{
                val titre = findViewById<EditText>(R.id.input_titre).text.toString()
                val contenu = findViewById<EditText>(R.id.input_contenu).text.toString()
                val date = findViewById<EditText>(R.id.input_date).text.toString()
                val selectedIdEmotionRadio = findViewById<RadioGroup>(R.id.emotion_group).checkedRadioButtonId



                // Gestion emotion non sélectionnée
                var emotion = 1

                if (selectedIdEmotionRadio != -1) {
                    val selectedRadio= findViewById<RadioButton>(selectedIdEmotionRadio)
                    emotion = selectedRadio.tag as Int
                }

                MemoriesStorage.get(applicationContext).insert(Memories(0, titre, imagePath, emotion, date, contenu))

                finish()


            }

        }




    //-------------------------------
    // GESTION DE L'IMPORTATION DE L'IMAGE
    //-------------------------------

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            val imageUri: Uri? = data?.data

            if (imageUri != null) {

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
    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }


    // TODO : Delete if not needed
    fun onImageViewClick(view: View) {
        pickImageFromGallery()
    }


    fun create_emotion_radio(emotions: JSONArray){
        val emotionRadioGroupContainer = findViewById<RadioGroup>(R.id.emotion_group)


        for (i in 0 until emotions.length()) {
            val emotion = emotions.getJSONObject(i)

            val radioButton = RadioButton(this)
            radioButton.text = emotion.getString("title")
            radioButton.tag = emotion.getInt("id")

            val emotionImage = ImageView(this)

            Glide.with(this)
                .load(emotion.getString("image_url"))
                .into(emotionImage)


            //emotionImage.layoutParams.height = 20
            //emotionImage.layoutParams.height = 20

            //emotionRadioGroupContainer.addView(emotionImage)
            emotionRadioGroupContainer.addView(radioButton)

        }

    }

    //-------------------------------
    // GESTION DE L'INPUT DATE
    //-------------------------------
    fun gestion_datePicker() {

        // code from : https://www.geeksforgeeks.org/how-to-popup-datepicker-while-clicking-on-edittext-in-android/
        // on below line we are initializing our variables.
        //dateEdt: EditText = findViewById<EditText>(R.id.input_date)

        val dateEdt: EditText = findViewById(R.id.input_date)

        // on below line we are adding
        // click listener for our edit text.
        dateEdt.setOnClickListener {

            // on below line we are getting
            // the instance of our calendar.
            val c = Calendar.getInstance()

            // on below line we are getting
            // our day, month and year.
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            // on below line we are creating a
            // variable for date picker dialog.
            val datePickerDialog = DatePickerDialog(
                // on below line we are passing context.
                this,
                { view, year, monthOfYear, dayOfMonth ->
                    // on below line we are setting
                    // date to our edit text.
                    val dat = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                    dateEdt.setText(dat)
                },
                // on below line we are passing year, month
                // and day for the selected date in our date picker.
                year,
                month,
                day
            )
            // at last we are calling show
            // to display our date picker dialog.

            datePickerDialog.show()
        }

    }



}