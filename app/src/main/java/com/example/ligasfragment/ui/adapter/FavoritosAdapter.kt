package com.example.ligasfragment.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ligasfragment.R
import com.example.ligasfragment.model.Equipo

class FavoritosAdapter(
    private val listaFavoritos: List<Equipo>
) : RecyclerView.Adapter<FavoritosAdapter.FavoritoViewHolder>() {

    class FavoritoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreEquipo: TextView = itemView.findViewById(R.id.nombreEquipo)
        val imagenEquipo: ImageView = itemView.findViewById(R.id.imagenEquipo)
        val favoritoImageView: ImageView = itemView.findViewById(R.id.imageViewFavorito)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_equipo, parent, false)
        return FavoritoViewHolder(view)
    }

    override fun getItemCount(): Int = listaFavoritos.size

    override fun onBindViewHolder(holder: FavoritoViewHolder, position: Int) {
        val equipo = listaFavoritos[position]
        holder.nombreEquipo.text = equipo.name
        Glide.with(holder.itemView.context).load(equipo.badgeUrl).into(holder.imagenEquipo)

        // Mostrar estrellita como favorito sin interacción
        holder.favoritoImageView.setImageResource(R.drawable.fav)
        holder.favoritoImageView.isClickable = false
        holder.favoritoImageView.isFocusable = false
    }
}
