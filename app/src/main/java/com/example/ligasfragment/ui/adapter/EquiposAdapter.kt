package com.example.ligasfragment.ui.adapter

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ligasfragment.R
import com.example.ligasfragment.model.Equipo

class EquiposAdapter(
    private val listaEquipos: List<Equipo>, // Asegúrate de que es 'Equipo' y no 'Team'
    private val sharedPreferences: SharedPreferences
) : RecyclerView.Adapter<EquiposAdapter.EquipoViewHolder>() {

    class EquipoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreEquipo: TextView = itemView.findViewById(R.id.nombreEquipo)
        val imagenEquipo: ImageView = itemView.findViewById(R.id.imagenEquipo)
        val favoritoImageView: ImageView = itemView.findViewById(R.id.imageViewFavorito)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EquipoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_equipo, parent, false)
        return EquipoViewHolder(view)
    }

    override fun getItemCount(): Int = listaEquipos.size

    override fun onBindViewHolder(holder: EquipoViewHolder, position: Int) {
        val equipo = listaEquipos[position]
        holder.nombreEquipo.text = equipo.name
        Glide.with(holder.itemView).load(equipo.badgeUrl).into(holder.imagenEquipo)

        // Verificar si el equipo está en favoritos
        val favs = sharedPreferences.getStringSet("favoritos", emptySet())!!
        val estrellas = if (favs.contains(equipo.name)) R.drawable.fav else R.drawable.no
        holder.favoritoImageView.setImageResource(estrellas)

        holder.favoritoImageView.setOnClickListener {
            val set = favs.toMutableSet()
            if (!set.add(equipo.name)) {
                set.remove(equipo.name)
            }
            sharedPreferences.edit().putStringSet("favoritos", set).apply()
            notifyItemChanged(position)
        }
    }
}
