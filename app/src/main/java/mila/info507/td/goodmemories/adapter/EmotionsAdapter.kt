package mila.info507.td.goodmemories.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import mila.info507.td.goodmemories.R

class Emotion (
    val id: Int,
    val image_url: String,
    val title: String,
){
    companion object{
        const val ID= "id"
        const val TITLE= "title"
        const val IMAGE_URL= "image_url"
    }
}

class EmotionsAdapter(private val dataSet: List<Emotion>): RecyclerView.Adapter<EmotionsAdapter.EmotionsHolder>() {

    //Gestion du click sur un élement de la liste.
    private lateinit var mListener: OnItemClickListener
    interface OnItemClickListener {
        fun OnItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener= listener
    }
    class EmotionsHolder(itemView: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val imageViewHolder: ImageView = itemView.findViewById(R.id.url_photo)
        val TextViewHolder: TextView = itemView.findViewById(R.id.emotion_title)

        init{
            itemView.setOnClickListener{
                listener.OnItemClick(adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmotionsHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return EmotionsHolder(view, mListener)
    }
    override fun onBindViewHolder(holder: EmotionsHolder, position: Int) {
        val emotion = dataSet[position]
        Glide.with(holder.itemView.context).load(emotion.image_url).into(holder.imageViewHolder)
        holder.TextViewHolder.text = emotion.title
    }
    override fun getItemCount(): Int {
        return dataSet.size
    }
}