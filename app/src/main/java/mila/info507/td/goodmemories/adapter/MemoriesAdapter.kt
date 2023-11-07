package mila.info507.td.goodmemories.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import mila.info507.td.goodmemories.R
import mila.info507.td.goodmemories.model.Memories
import mila.info507.td.goodmemories.request.RequestEmotions

class MemoriesAdapter(private val dataSet: List<Memories>): RecyclerView.Adapter<MemoriesAdapter.MemoriesHolder>() {

    //Gestion du click sur un élement de la liste.
    private lateinit var mListener: OnItemClickListener

    // Nécessaire pour mettre en place le rem
    private lateinit var parent_memorie: ViewGroup;

    // Permet la gestion du click sur un élément du RecyclerView, la fonction est redéfinit lors de la mise en place du click listener
    interface OnItemClickListener {
        fun OnItemClick(position: Int)
    }

    // Fonction qui permet de redéfinir le OnItemClickListener et qu'il soit stocké dans l'adapteur
    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener= listener
    }

    //==============================================
    // Memorie holder
    //==============================================
    class MemoriesHolder(itemView: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {
        // Récupère les "champs" à remplir
        val photo: ImageView = itemView.findViewById(R.id.memorie_photo)
        val title: TextView = itemView.findViewById(R.id.memorie_title)
        val date: TextView = itemView.findViewById(R.id.memorie_date)
        val emotion: ImageView = itemView.findViewById(R.id.memorie_humeur)

        init{
            itemView.setOnClickListener{
                listener.OnItemClick(adapterPosition)
            }
        }

    }

    // Est appelée quand un nouvel item doit etre créé dans la vue du RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoriesHolder {
        parent_memorie = parent
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_memorie, parent, false)
        return MemoriesHolder(view, mListener)
    }

    // Remplis les infos de l'élément
    override fun onBindViewHolder(holder: MemoriesHolder, position: Int) {
        // On récupère le memorie
        val memo = dataSet[position]

        // On remplis les champs
        Glide.with(holder.itemView.context).load(dataSet[position].photo).transform(CenterCrop()).into(holder.photo)
        holder.title.text = memo.title
        holder.date.text = memo.date

        // On ajoute l'image de l'émotion
        val reEm: RequestEmotions = RequestEmotions(parent_memorie.context)
        reEm.getEmotionImageUrlById(memo.emotion){ imageUrl ->  if (imageUrl != "") {
            Glide.with(holder.itemView.context).load(imageUrl).into(holder.emotion)
        }}


    }

    // Donne la taille de la liste de memorie
    override fun getItemCount(): Int {
        return dataSet.size
    }
}