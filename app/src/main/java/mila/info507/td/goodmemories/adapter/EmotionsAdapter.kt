package mila.info507.td.goodmemories.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import mila.info507.td.goodmemories.R

//==============================================
// Model Emotion
//==============================================

class Emotion (
    val id: Int,
    val image_url: String,
    val title: String,
){
    // Faciliter la récupération des valeurs
    companion object{
        const val ID= "id"
        const val TITLE= "title"
        const val IMAGE_URL= "image_url"
    }
}

//==============================================
// Emotion adapter
//==============================================
class EmotionsAdapter(private val dataSet: List<Emotion>): RecyclerView.Adapter<EmotionsAdapter.EmotionsHolder>() {

    //permet gestion du click sur un élement de la liste.
    private lateinit var mListener: OnItemClickListener

    // Permet la gestion du click sur un élément du RecyclerView, la fonction est redéfinit lors de la mise en place du click listener
    interface OnItemClickListener {
        fun OnItemClick(position: Int)
    }


    // Fonction qui permet de redéfinir le OnItemClickListener et qu'il soit stocké dans l'adapteur
    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener= listener
    }

    //==============================================
    // Emotion holder
    //==============================================
    class EmotionsHolder(itemView: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {

        // Récupère les "champs" à remplir
        val imageViewHolder: ImageView = itemView.findViewById(R.id.url_photo)
        val TextViewHolder: TextView = itemView.findViewById(R.id.emotion_title)

        init{
            itemView.setOnClickListener{
                listener.OnItemClick(adapterPosition)
            }
        }

    }

    // Est appelée quand un nouvel item doit etre créé dans la vue du RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmotionsHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return EmotionsHolder(view, mListener)
    }


    // Remplis les infos de l'élément
    override fun onBindViewHolder(holder: EmotionsHolder, position: Int) {
        // On récupere l'émotion
        val emotion = dataSet[position]

        // On ajoute la bonne image et le bon titre
        Glide.with(holder.itemView.context).load(emotion.image_url).into(holder.imageViewHolder)
        holder.TextViewHolder.text = emotion.title
    }

    // Donne la taille de la liste d'émotions
    override fun getItemCount(): Int {
        return dataSet.size
    }
}