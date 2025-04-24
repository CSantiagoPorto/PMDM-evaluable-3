package com.example.ligasfragment.ui.adapter

import android.content.SharedPreferences
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
    private val listaEquipos: List<Equipo>,
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

        val favoritos = sharedPreferences.getStringSet("favoritos", emptySet())?.toMutableSet() ?: mutableSetOf()
        val esFavorito = favoritos.contains(equipo.name)
        holder.favoritoImageView.setImageResource(if (esFavorito) R.drawable.fav else R.drawable.no)

        holder.favoritoImageView.setOnClickListener {//El escuchador
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            val database=FirebaseDatabase.getInstance()
            val referencia =database.getReference("equipos Favoritos").child(userId ?: "")
            if (esFavorito) {
                favoritos.remove(equipo.name)
                Toast.makeText(holder.itemView.context, "${equipo.name} eliminado de favoritos", Toast.LENGTH_SHORT).show()

                if(userId !=null){referencia.child(equipo.name).removeValue()
                }else{favoritos.add(equipo.name)
                    Toast.makeText(holder.itemView.context,"${equipo.name}agregado con exito a favcritos", Toast.LENGTH_SHORT).show()
                    if (userId!=null){referencia.child(equipo.name).setValue(equipo)}
                    notifyItemChanged(position)
                }

               /* holder.favoritoImageView.setImageResource(R.drawable.no)
                userId?.let {
                    val ref = FirebaseDatabase.getInstance().getReference("equiposFavoritos").child(it)
                    ref.child(equipo.name).removeValue()
                }
                Toast.makeText(holder.itemView.context, "${equipo.name} eliminado de favoritos", Toast.LENGTH_SHORT).show()
            } else {
                favoritos.add(equipo.name)
                sharedPreferences.edit().putStringSet("favoritos", favoritos).apply()
                holder.favoritoImageView.setImageResource(R.drawable.fav)
                userId?.let {
                    val ref = FirebaseDatabase.getInstance().getReference("equiposFavoritos").child(it)
                    ref.child(equipo.name).setValue(equipo)
                }*/
                //Toast.makeText(holder.itemView.context, "${equipo.name} agregado a favoritos", Toast.LENGTH_SHORT).show()
            }
            notifyItemChanged(position)
        }
    }
}
