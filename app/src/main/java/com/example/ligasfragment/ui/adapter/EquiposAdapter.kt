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
        val claveEquipo = equipo.idTeam//Para FB esta va a ser la clave

        val ref = FirebaseDatabase.getInstance("https://ligasfragment-default-rtdb.europe-west1.firebasedatabase.app/").reference.child("usuarios").child(userId ?: "").child("listaFav")
        
        // Estado inicial: comprobar si ya es favorito y pintar la estrella
        ref.get().addOnSuccessListener { snapshot->
            for(child in snapshot.children){
                val equipo =child.getValue(Equipo::class.java)
                Log.v("equiposAdapter","equipo polsado")
                if(claveEquipo==equipo!!.idTeam) {
                    holder.favoritoImageView.setImageResource(R.drawable.fav)
                }
            }

        }

        // Al hacer clic en la estrellita
        holder.favoritoImageView.setOnClickListener {
            Log.e("EquiposAdapter", "Estrellita pulsada en ${equipo.name}")
            //ref.child(claveEquipo).setValue(equipo)
            ref.child(claveEquipo).get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    ref.child(claveEquipo).removeValue()
                    holder.favoritoImageView.setImageResource(R.drawable.no)
                } else {
                    ref.child(claveEquipo).setValue(equipo)//guardo el equipo completo en Firebase
                    holder.favoritoImageView.setImageResource(R.drawable.fav)
                }
            }.addOnFailureListener {
                Log.e("EquiposAdapter", "Error leyendo favorito", it)
            }
            holder.favoritoImageView.setImageResource(R.drawable.fav)
        }
    }
}
