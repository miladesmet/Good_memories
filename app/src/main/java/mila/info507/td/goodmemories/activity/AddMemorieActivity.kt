package mila.info507.td.goodmemories.activity

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import android.Manifest
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

class AddMemorieActivity : AppCompatActivity() {

    private val PICK_IMAGE_FROM_GALLERY = 1
    private val PICK_IMAGE_FROM_CAMERA = 2

    // Pour récuperer le path de l'image ajoutée
    private var imagePath: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_memorie)


        // Demande de permission à la camera


        //PHOTO
        val bouton_ajout_image =  findViewById<ImageView>(R.id.ajouter_image)

        bouton_ajout_image.setOnClickListener{
            showImagePickerDialog()
        }


        // Récuperation de la liste des émotions pour le radio

        val rem : RequestEmotions = RequestEmotions(applicationContext)
        val emotions = rem.getEmotions({response -> create_emotion_radio(response)})


        gestion_datePicker()

            //------------
            // Mise en place du bouton annuler
            // -----------
            val annuler_button : View = findViewById<TextView>(R.id.annuler)
             annuler_button.setOnClickListener{
                 finish()
             }
            //------------
            // Mise en place du bouton enregistrer
            // -----------
            val enregistrer_button : View = findViewById<TextView>(R.id.Enregistrer)

            enregistrer_button.setOnClickListener{

                // Récupération du contenu des input
                val titre = findViewById<EditText>(R.id.input_titre).text.toString()
                val contenu = findViewById<EditText>(R.id.input_contenu).text.toString()
                val date = findViewById<EditText>(R.id.input_date).text.toString()
                val selectedIdEmotionRadio = findViewById<RadioGroup>(R.id.emotion_group).checkedRadioButtonId

                // Validation du formulaire
                // Vérification que tout est bien remplis

                var validation : Boolean = true
                if (titre.isEmpty()){
                    Toast.makeText(applicationContext, "Veuillez remplir le titre", Toast.LENGTH_SHORT).show()
                    validation = false
                }

                if (date.isEmpty()){
                    Toast.makeText(applicationContext, "Veuillez remplir la date", Toast.LENGTH_SHORT).show()
                    validation = false
                }

                if (contenu.isEmpty()){
                    Toast.makeText(applicationContext, "Veuillez remplir le contenu", Toast.LENGTH_SHORT).show()
                    validation = false
                }



                // Si tout est bien remplis on ajoute le memorie
                if (validation){
                    // Gestion emotion non sélectionnée
                    var emotion = 1

                    if (selectedIdEmotionRadio != -1) {
                        val selectedRadio = findViewById<RadioButton>(selectedIdEmotionRadio)
                        emotion = selectedRadio.tag as Int
                    }

                    // Ajout du memorie dans le json mémoire
                    MemoriesStorage.get(applicationContext)
                        .insert(Memories(0, titre, imagePath, emotion, date, contenu))

                    // Toast pour informer l'utilisateur que tout c'est bien déroulé
                    Toast.makeText(
                        applicationContext,
                        "Le memorie a bien été ajouté",
                        Toast.LENGTH_SHORT
                    ).show()

                    // On ouvre l'activité congrat
                    val intent = Intent(this, CongratulationActivity::class.java)
                    startActivity(intent)

                    // fin de l'activité
                    finish()


                }

            }

        }




    //-------------------------------
    // GESTION DE L'IMPORTATION DE L'IMAGE
    //-------------------------------

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_FROM_GALLERY && resultCode == RESULT_OK) {
            val imageUri: Uri? = data?.extras?.get("data") as Uri

            if (imageUri != null) {
                val directory = File(filesDir, "images")
                if (!directory.exists()) {
                    directory.mkdirs()
                }

                val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                val fileName = "IMG_$timestamp.jpg"
                val file = File(directory, fileName)

                try {
                    val inputStream = contentResolver.openInputStream(imageUri)
                    val outputStream = FileOutputStream(file)

                    val buffer = ByteArray(1024)
                    var bytesRead: Int
                    while (true) {
                        bytesRead = inputStream?.read(buffer) ?: -1
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

                imagePath = file.absolutePath

                val bouton_ajout_image = findViewById<ImageView>(R.id.ajouter_image)
                bouton_ajout_image.setOnClickListener {
                    showImagePickerDialog()
                }

                Glide.with(this)
                    .load(imagePath)
                    .into(bouton_ajout_image)
            }
        } else if (requestCode == PICK_IMAGE_FROM_CAMERA && resultCode == RESULT_OK) {

            println("=====================")
            println(data)
            println("=====================")
            println(data?.data)
            println("=====================")
            println(data?.extras)
            println("=====================")
            println(data?.extras?.get("data"))


            if (data != null ) {
                println("=====================")
                println("On est dedans")
                println("=====================")
                val imageUri: Uri = data?.extras?.get("data") as Uri
                println("=====================")
                println(imageUri)
                val directory = File(filesDir, "images")
                if (!directory.exists()) {
                    directory.mkdirs()
                }

                val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                val fileName = "IMG_$timestamp.jpg"
                val file = File(directory, fileName)

                try {
                    println("=====================")
                    println("On est dedans 2")
                    println("=====================")
                    val inputStream = contentResolver.openInputStream(imageUri)
                    val outputStream = FileOutputStream(file)

                    val buffer = ByteArray(1024)
                    var bytesRead: Int
                    while (true) {
                        bytesRead = inputStream?.read(buffer) ?: -1
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

                imagePath = file.absolutePath

                val bouton_ajout_image = findViewById<ImageView>(R.id.ajouter_image)
                bouton_ajout_image.setOnClickListener {
                    showImagePickerDialog()
                }

                Glide.with(this)
                    .load(imagePath)
                    .into(bouton_ajout_image)
            }
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_FROM_GALLERY)
    }


    // TODO : Delete if not needed
    fun onImageViewClick(view: View) {
        pickImageFromGallery()
    }


    fun create_emotion_radio(emotions: JSONArray){

        // On selectionne le RadioGroupContainer
        val emotionRadioGroupContainer = findViewById<RadioGroup>(R.id.emotion_group)

        // Pour chaque emotion on cree un radio button qu'on ajoute dans le RadioGroupContainer
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


    //----------------------------------------------------------------
    // Gestion choix caméra
    //----------------------------------------------------------------


    // Dialog pour demander de choisir ou récupérer la photo
    private fun showImagePickerDialog() {
        // Création du tableau des options
        val options = arrayOf("Galerie", "Appareil photo")
        // Création de la boîte de dialogue
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choisir une image depuis:")
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> pickImageFromGallery()
                1 -> captureImageFromCamera()
            }
        }
        // Affichage de la boîte de dialogue
        builder.show()
    }

    // Gestion du choix d'ouvrir la caméra
    private fun captureImageFromCamera() {
        val perm = checkPermission(Manifest.permission.CAMERA)
        if (perm) {
            try {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (intent.resolveActivity(packageManager) != null) {
                    startActivityForResult(intent, PICK_IMAGE_FROM_CAMERA)
                } else {
                    Toast.makeText(
                        this,
                        "L'application de l'appareil photo n'est pas disponible.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(
                    this,
                    "Une erreur est survenue lors de l'ouverture de l'appareil photo.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    private fun checkPermission(permission: String): Boolean {
        var res = true
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                ActivityCompat.requestPermissions(this, arrayOf(permission), 0)
            }
            res = false
        }
        return res
    }


}