package com.example.ligasfragment.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ligasfragment.R
import com.example.ligasfragment.model.Equipo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class EquiposAdapter(
    private val listaEquipos: List<Equipo>
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

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("equiposFavoritos").child(userId ?: "")

        // Se establece la estrella por defecto como no-favorito
        holder.favoritoImageView.setImageResource(R.drawable.no)

        // Escuchador
        holder.favoritoImageView.setOnClickListener {
            if (userId == null) {
                Toast.makeText(holder.itemView.context, "Debes estar logueado", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Consulta actual del equipo para saber si está o no en favoritos
            ref.child(equipo.name).get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    // Eliminar de favoritos
                    ref.child(equipo.name).removeValue()
                    holder.favoritoImageView.setImageResource(R.drawable.no)
                    Toast.makeText(holder.itemView.context, "${equipo.name} eliminado de favoritos", Toast.LENGTH_SHORT).show()
                } else {
                    // Agregar a favoritos
                    ref.child(equipo.name).setValue(equipo)
                    holder.favoritoImageView.setImageResource(R.drawable.fav)
                    Toast.makeText(holder.itemView.context, "${equipo.name} agregado a favoritos", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
