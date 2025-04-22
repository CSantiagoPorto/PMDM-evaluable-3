package com.example.ligasfragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ligasfragment.model.Liga

class LigasAdapter(private val listaLigas: List<Liga>) :
    RecyclerView.Adapter<LigasAdapter.LigaViewHolder>() {

    class LigaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreTextView: TextView = itemView.findViewById(R.id.textViewNombreLiga) // Asegúrate de que tienes este ID en tu archivo item_liga.xml
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LigaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_liga, parent, false)  // Aquí cargas el layout de un item
        return LigaViewHolder(view)  // Crea el ViewHolder a partir de la vista inflada
    }

    override fun onBindViewHolder(holder: LigaViewHolder, position: Int) {
        // Asegúrate de que las propiedades de Liga están bien definidas
        val liga = listaLigas[position]
        holder.nombreTextView.text = liga.nombre  // Asigna el nombre de la liga al TextView
    }

    override fun getItemCount(): Int = listaLigas.size  // Devuelve el tamaño de la lista de ligas
}
