package mila.info507.td.goodmemories.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import mila.info507.td.goodmemories.R

class MemoriesAdapter: RecyclerView.Adapter<MemoriesAdapter.ExpenseHolder>() {
    class ExpenseHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val photo: ImageView = itemView.findViewById(R.id.memorie_photo)
        val title: TextView = itemView.findViewById(R.id.memorie_title)
        val date: TextView = itemView.findViewById(R.id.memorie_date)
        val humeur: ImageView = itemView.findViewById(R.id.memorie_humeur)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_memorie, parent, false)
        return ExpenseHolder(view)
    }
    override fun onBindViewHolder(holder: ExpenseHolder, position: Int) {
        holder.photo.setImageResource(R.drawable.cercle_shape)
        holder.title.text = "LE TITRE"
        holder.date.text = "maintenant"
        holder.photo.setImageResource(R.drawable.cercle_shape)
    }
    override fun getItemCount(): Int {
        return 10
    }
}