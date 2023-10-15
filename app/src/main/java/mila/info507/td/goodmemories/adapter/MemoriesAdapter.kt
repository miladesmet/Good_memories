package mila.info507.td.goodmemories.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import mila.info507.td.goodmemories.R
import mila.info507.td.goodmemories.model.Memories

class MemoriesAdapter(private val dataSet: List<Memories>): RecyclerView.Adapter<MemoriesAdapter.MemoriesHolder>() {

    //Gestion du click sur un élement de la liste.
    private lateinit var mListener: OnItemClickListener
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
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_memorie, parent, false)
        return MemoriesHolder(view, mListener)
    }
    override fun onBindViewHolder(holder: MemoriesHolder, position: Int) {
        holder.photo.setImageResource(R.drawable.cercle_shape)
        holder.title.text = dataSet[position].title
        holder.date.text = dataSet[position].date
        holder.photo.setImageResource(R.drawable.cercle_shape)
    }
    override fun getItemCount(): Int {
        return dataSet.size
    }
}