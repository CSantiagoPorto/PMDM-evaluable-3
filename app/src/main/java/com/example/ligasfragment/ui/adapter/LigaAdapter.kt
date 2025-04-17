package com.example.ligasfragment.ui.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ligasfragment.R

import com.example.ligasfragment.model.Liga

class LigaAdapter(private val listaLigas: List<Liga>) :
    RecyclerView.Adapter<LigaAdapter.LigaViewHolder>() {

    class LigaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreTextView: TextView = itemView.findViewById(R.id.textViewNombreLiga)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LigaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_liga, parent, false)
        return LigaViewHolder(view)
    }

    override fun onBindViewHolder(holder: LigaViewHolder, position: Int) {
        val liga = listaLigas[position]
        holder.nombreTextView.text = liga.nombre
    }

    override fun getItemCount(): Int = listaLigas.size
}
