package mila.info507.td.goodmemories.activity


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
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

    private lateinit var list_emotions : List<Emotion> //- Liste d'emotion récupérée en externe
    private lateinit var list : RecyclerView
    private lateinit var adapter: MemoriesAdapter
    private lateinit var adapter_emotion: EmotionsAdapter
    lateinit var swipeRefreshLayout: SwipeRefreshLayout



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accueil)

        // On va chercher les émotions
        val rem = RequestEmotions(applicationContext)
        // On les mets dans lise_emotion
        rem.getEmotions { response -> create_emotion_list(response) }

        // Création de certains mémories par défaut pour la démo - à enlever pour une vraie utilisation
        var len_memories = MemoriesStorage.get(applicationContext).size()


        // Création d'une BdD exemple
        if (len_memories ==0) {
            MemoriesStorage.get(applicationContext).insert(
                Memories(
                    0,
                    "Ma première randonnée en montagne",
                    "https://www.auvergne-destination.com/wp-content/uploads/2021/10/randonne-sancy-conseil-dpartemental-140719-mickal-mussard0028-1600x900.jpg",
                    1,
                    "10-10-2023",
                    "Aujourd'hui, j'ai fait ma première randonnée en montagne. J'ai longtemps rêvé de gravir ces sommets majestueux, et enfin, l'occasion s'est présentée. La montée a été exigeante, mais chaque pas en valait la peine. Le vent frais sur mon visage, le silence paisible de la nature et les paysages à couper le souffle ont créé une expérience inoubliable. J'ai pris cette photo incroyable depuis le sommet pour immortaliser ce moment magique."
                )
            )
            MemoriesStorage.get(applicationContext).insert(
                Memories(
                    0,
                    "Anniversaire surprise de Sarah",
                    "https://tadaaz.fr/blog/wp-content/uploads/2014/08/anniversaire-surprise.jpg",
                    4,
                    "10-09-2023",
                    "Nous avons organisé une fête surprise incroyable pour l'anniversaire de Sarah. C'était une journée de planification secrète intense, mais la surprise sur son visage en valait la peine. La maison était décorée avec soin, et tous ses amis et sa famille étaient présents pour célébrer. La joie et l'émotion sur son visage lorsque nous avons allumé les bougies du gâteau resteront gravées dans ma mémoire. Nous avons partagé des rires, des histoires et des souvenirs précieux"
                )
            )
            MemoriesStorage.get(applicationContext).insert(
                Memories(
                    0,
                    "Dernier jour à l'université",
                    "https://www.southernliving.com/thmb/Nexxnzduqs1Nlrdf11M9E5k1Mnk=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/GettyImages-1162745842-2000-a03c9bdc95c747aeaa34108f18fb7018.jpg",
                    2,
                    "21-07-2023",
                    "Mon dernier jour à l'université est arrivé, et les émotions sont fortes. C'est le moment où je ferme un chapitre de ma vie et en commence un nouveau. Je me souviens des années d'études, des amis fidèles que j'ai rencontrés et des défis surmontés. C'est un mélange de joie pour ce qui est à venir et de nostalgie pour ce qui a été. Les adieux, les sourires et les larmes ont marqué ce jour spécial."
                )
            )
            MemoriesStorage.get(applicationContext).insert(
                Memories(
                    0,
                    "Journée en famille au parc d'attractions",
                    "https://www.uzes-pontdugard.com/wp-content/uploads/2023/06/16050300-diaporama-1200x8002-1.jpg",
                    4,
                    "10-09-2023",
                    "Nous avons passé une journée extraordinaire en famille au parc d'attractions. Les rires, les manèges excitants et les délicieuses glaces ont créé une atmosphère de pur bonheur. Les enfants étaient enthousiastes, les parents se sont amusés comme des enfants, et nous avons créé des souvenirs en famille qui dureront toute une vie. C'était une journée de détente, de divertissement et de complicité."
                )
            )
            MemoriesStorage.get(applicationContext).insert(
                Memories(
                    0,
                    "Mon premier concert en plein air",
                    "https://weezevent.com/wp-content/uploads/2023/01/13120315/reglementation-concert-plein-air.jpg",
                    4,
                    "02-09-2023",
                    "Hier soir, j'ai assisté à mon premier concert en plein air, et c'était une expérience incroyable. Le ciel était clair, les étoiles brillaient, et la musique résonnait dans l'air. Mon groupe préféré était sur scène, et je me sentais en symbiose avec la foule enthousiaste. Les chansons emblématiques, la danse et la camaraderie avec d'autres fans ont créé une ambiance électrique. J'ai pris une photo mémorable lorsque les feux d'artifice ont éclairé le ciel pour clore le spectacle. C'était un mélange d'émotions, de bonheur pur et d'excitation qui restera gravé dans ma mémoire. "
                )
            )}

        //Mise en place du refresh
        swipeRefreshLayout = findViewById(R.id.swipe)
        swipeRefreshLayout.setOnRefreshListener{
            loadAllMemories()
            rem.getEmotions { response -> create_emotion_list(response) }
            // On arrete l'animation de refresh
            swipeRefreshLayout.isRefreshing = false
        }

        //création du RecyclerView avec tous les memories
        list = findViewById(R.id.memories_list)

        // On load une premiere fois tous les memories
        loadAllMemories()

        //---------------------
        // Mise en place bouton tous
        //---------------------
        findViewById<TextView>(R.id.Tous).setOnClickListener {
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
            // Supprime le message de liste vide s'il existe
            removeEmptyMessage()

            // On donne la liste de tous les memories au constructeur de MemoriesAdapter et on le stocke dans la variable adapter
            adapter = MemoriesAdapter(MemoriesStorage.get(applicationContext).findAll())
            // On relie cet adapter à la recyclerView
            list.adapter = adapter

            //-------------------------------
            // Gestion du click sur un memorie du RecyclerView
            //-------------------------------
            // Ici on utilise le fait que l'adapteur utilise une interface OnItemClickListener
            adapter.setOnItemClickListener(object : MemoriesAdapter.OnItemClickListener {
                // On redefinit la methode onItemClick
                override fun OnItemClick(position: Int) {
                    val intent = Intent(this@AccueilActivity, MemorieActivity::class.java)
                    intent.putExtra(
                        "position",
                        MemoriesStorage.get(applicationContext).findAll()[position].id
                    )
                    startActivity(intent)
                }
            })

        // Dans le cas où il n'y a pas de memorie à afficher nous créeons un message l'indiquant
        if (MemoriesStorage.get(applicationContext).size() ==0) {
            empty_memorie()
        }
    }

    private fun loadMemoriesByEmotion(id: Int) {
        // Supprime le message "vide" s'il existe
        removeEmptyMessage()

        // On donne la liste de tous les memories ayant l'emotion donnée au constructeur de MemoriesAdapter et on le stocke dans la variable adapter
        adapter = MemoriesAdapter(MemoriesStorage.get(applicationContext).findAllByEmotion(id))
        // On relie cet adapter à la recyclerView
        list.adapter=  adapter

        //-------------------------------
        // Gestion du click sur un memorie du RecyclerView
        //-------------------------------
        // Ici on utilise le fait que l'adapteur utilise une interface OnItemClickListener
        adapter.setOnItemClickListener(object : MemoriesAdapter.OnItemClickListener{
            override fun OnItemClick(position: Int) {
                val intent =Intent(this@AccueilActivity, MemorieActivity::class.java)
                intent.putExtra("position", MemoriesStorage.get(applicationContext).findAllByEmotion(id)[position].id)
                startActivity(intent)
            }

        })

        // Dans le cas où il n'y a pas de memorie à afficher nous créeons un message l'indiquant
        if (MemoriesStorage.get(applicationContext).findAllByEmotion(id).isEmpty()) {
            empty_memorie()
        }
    }

    private fun loadAllEmotion() {
        // Supprime le message "vide" s'il existe
        removeEmptyMessage()

        // On donne la liste de toutes les émotions au constructeur EmotionsAdapter
        adapter_emotion = EmotionsAdapter(list_emotions)

        // On relie cet adapter à la recyclerView
        list.adapter=  adapter_emotion

        //-------------------------------
        // Gestion du click sur une émotion du RecyclerView
        //-------------------------------
        // Ici on utilise le fait que l'adapteur utilise une interface OnItemClickListener
        adapter_emotion.setOnItemClickListener(object : EmotionsAdapter.OnItemClickListener{
            override fun OnItemClick(position: Int) {
                loadMemoriesByEmotion(list_emotions[position].id)
            }

        })
    }

    // fonction callback
    fun create_emotion_list(emotions: JSONArray){

        // initialisation de la liste d'emotion
        val emotions_list : ArrayList<Emotion> = ArrayList()

        // recupere chaque emotion du json et ça l'ajoute dans la liste
        for (i in 0 until emotions.length()) {
            val emotion = emotions.getJSONObject(i)
            emotions_list.add(Emotion(emotion.getInt("id"), emotion.getString("image_url"), emotion.getString("title")))
        }

        if (emotions.length()!=0) {
            list_emotions = emotions_list.toList()
        }
    }

        fun empty_memorie(){
            // On récupere le LinearLayout central
            var LinearLayoutCentre = findViewById<LinearLayout>(R.id.layout_centre)

            // On crée un élément TextView
            val TextEmpty = TextView(this)
            TextEmpty.text = "Vous n'avez pas encore de memories ici... Ajoutez-en !"
            TextEmpty.gravity = Gravity.CENTER
            TextEmpty.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18F)
            TextEmpty.setTextColor(Color.GRAY)

            //On l'ajoute dans le LinearLayout central
            LinearLayoutCentre.addView(TextEmpty)
        }

    private fun removeEmptyMessage() {
        // On récupere le LinearLayout central
        val LinearLayoutCentre = findViewById<LinearLayout>(R.id.layout_centre)
        // On suppime tout ces "enfants"
        LinearLayoutCentre.removeAllViews()
    }





}