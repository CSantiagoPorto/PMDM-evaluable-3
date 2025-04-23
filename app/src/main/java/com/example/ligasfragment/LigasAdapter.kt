package com.example.ligasfragment

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ligasfragment.model.Liga

class LigasAdapter(
    private val listaLigas: List<Liga>,
    private val sharedPreferences: SharedPreferences,
    private val listener: OnFavoritoClickListener
) : RecyclerView.Adapter<LigasAdapter.LigaViewHolder>() {

    interface OnFavoritoClickListener {
        fun onFavoritoClick(liga: Liga, position: Int)
    }

    class LigaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreTextView: TextView    = itemView.findViewById(R.id.NombreLiga)
        val favoritoImageView: ImageView = itemView.findViewById(R.id.imageViewFavorito)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LigaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_liga, parent, false)
        return LigaViewHolder(view)
    }

    override fun getItemCount(): Int = listaLigas.size

    override fun onBindViewHolder(holder: LigaViewHolder, position: Int) {
        val liga = listaLigas[position]
        holder.nombreTextView.text = liga.nombre

        // ¿Está en favoritos?
        val favs = sharedPreferences
            .getStringSet("favoritos", emptySet())!!
        val estrellas = if (favs.contains(liga.nombre)) R.drawable.fav else R.drawable.no


       // holder.favoritoImageView.setOnClickListener(listener.onFavoritoClick(liga,position))

       // Glide.with(holder.itemView)
        //    .load(liga.logoUrl)
          //  .placeholder(R.drawable.no)
            //.into(holder.favoritoImageView)
        holder.favoritoImageView.setImageResource(estrellas)
        holder.favoritoImageView.setOnClickListener {
            listener.onFavoritoClick(liga, position)

        }
    }
}
