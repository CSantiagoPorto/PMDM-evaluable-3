package com.example.ligasfragment.ui.adapter

import android.util.Log
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
        val claveEquipo = equipo.name.replace(".", "_").replace("/", "_") // Firebase no acepta estos caracteres
        val ref = FirebaseDatabase.getInstance().getReference("equiposFavoritos").child(userId ?: "")

        // Estado inicial: comprobar si ya es favorito y pintar la estrella
        ref.child(claveEquipo).get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                holder.favoritoImageView.setImageResource(R.drawable.fav)
            } else {
                holder.favoritoImageView.setImageResource(R.drawable.no)
            }
        }.addOnFailureListener {
            Log.e("EquiposAdapter", "Error leyendo favorito", it)
        }

        // Al hacer clic en la estrellita
        holder.favoritoImageView.setOnClickListener {
            if (userId == null) {
                Toast.makeText(holder.itemView.context, "Debes estar logueado", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            ref.child(claveEquipo).get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    // Eliminar de favoritos
                    ref.child(claveEquipo).removeValue()
                    holder.favoritoImageView.setImageResource(R.drawable.no)
                    Toast.makeText(holder.itemView.context, "${equipo.name} eliminado de favoritos", Toast.LENGTH_SHORT).show()
                } else {
                    // Agregar a favoritos
                    ref.child(claveEquipo).setValue(equipo)
                    holder.favoritoImageView.setImageResource(R.drawable.fav)
                    Toast.makeText(holder.itemView.context, "${equipo.name} agregado a favoritos", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Log.e("EquiposAdapter", "Error al actualizar favorito", it)
            }
        }
    }
}
