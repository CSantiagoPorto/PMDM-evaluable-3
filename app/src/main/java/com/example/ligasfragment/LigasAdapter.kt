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
import com.example.ligasfragment.ui.fragment.LigasFragment

class LigasAdapter(private val listaLigas: MutableList<Liga>, private val sharedPreferences : SharedPreferences) :


    RecyclerView.Adapter<LigasAdapter.LigaViewHolder>() {

    class LigaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreTextView: TextView = itemView.findViewById(R.id.NombreLiga)
        val favoritoImageView:ImageView=itemView.findViewById(R.id.imageViewFavorito)
      //
    //  val logoImagenView:ImageView=itemView.findViewById(R.id.logo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LigaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_liga, parent, false)  // Aquí cargo el layout de un item
        return LigaViewHolder(view)  // Crea el ViewHolder a partir de la vista inflada
    }

    override fun onBindViewHolder(holder: LigaViewHolder, position: Int) {


        val liga = listaLigas[position]
        holder.nombreTextView.text = liga.nombre  // Asigna el nombre de la liga al TextView

        //Ahora necesito saber si la liga está en favoritos y cambiar el icono
        // Verificamos si la liga está en favoritos y cambiamos el ícono
        val favoritos = sharedPreferences.getStringSet("favoritos", mutableSetOf()) ?: mutableSetOf()
        if (favoritos.contains(liga.nombre)) {
            Glide.with(holder.favoritoImageView.context)
                .load(R.drawable.fav)  // Aquí carga la imagen de la estrella favorita
                .into(holder.favoritoImageView)
        } else {
            Glide.with(holder.favoritoImageView.context)
                .load(R.drawable.no)  // Aquí carga la imagen de la estrella no favorita
                .into(holder.favoritoImageView)
        }

    }

    override fun getItemCount(): Int = listaLigas.size  // Devuelve el tamaño de la lista de ligas



}
