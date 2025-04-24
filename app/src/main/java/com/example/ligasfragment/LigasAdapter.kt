package com.example.ligasfragment

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ligasfragment.model.Liga

class LigasAdapter(
    private val listaLigas: List<Liga>,
    private val sharedPreferences: SharedPreferences,
    private val listener: OnFavoritoClickListener
) : RecyclerView.Adapter<LigasAdapter.LigaViewHolder>() {

    interface OnFavoritoClickListener {
        fun onFavoritoClick(liga: Liga, position: Int)
        fun onLigaClick(liga:Liga)
    }

    class LigaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreTextView: TextView    = itemView.findViewById(R.id.NombreLiga)//este es el textView donde voy a mostrar la liga
        val favoritoImagen: ImageView = itemView.findViewById(R.id.imageViewFavorito)
        val verLigaButton: Button = itemView.findViewById(R.id.btnLiga)
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

        // miro si está en favoritos
        val favs = sharedPreferences
            .getStringSet("favoritos", emptySet())!!
        val estrellas = if (favs.contains(liga.nombre)) R.drawable.fav else R.drawable.no


       // holder.favoritoImageView.setOnClickListener(listener.onFavoritoClick(liga,position))

       // Glide.with(holder.itemView)
        //    .load(liga.logoUrl)
          //  .placeholder(R.drawable.no)
            //.into(holder.favoritoImageView)
        holder.favoritoImagen.setImageResource(estrellas)
        holder.favoritoImagen.setOnClickListener {
            listener.onFavoritoClick(liga, position)


        }
        //Este es el callback del botón
        holder.verLigaButton.setOnClickListener {
            listener.onLigaClick(liga)


        }
    }
}
