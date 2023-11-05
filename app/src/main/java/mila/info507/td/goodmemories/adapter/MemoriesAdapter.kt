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

    interface OnItemClickListener {
        fun OnItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener= listener
    }
    class MemoriesHolder(itemView: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoriesHolder {
        parent_memorie = parent
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_memorie, parent, false)
        return MemoriesHolder(view, mListener)
    }
    override fun onBindViewHolder(holder: MemoriesHolder, position: Int) {
        val memo = dataSet[position]
        Glide.with(holder.itemView.context).load(dataSet[position].photo).transform(CenterCrop()).into(holder.photo)
        holder.title.text = memo.title
        holder.date.text = memo.date

        val reEm: RequestEmotions = RequestEmotions(parent_memorie.context)
        reEm.getEmotionImageUrlById(memo.emotion){ imageUrl ->  if (imageUrl != "") {
            Glide.with(holder.itemView.context).load(imageUrl).into(holder.emotion)
        }}


    }
    override fun getItemCount(): Int {
        return dataSet.size
    }
}