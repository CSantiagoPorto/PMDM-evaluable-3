package com.example.ligasfragment.ui.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ligasfragment.databinding.FragmentFavoritosBinding
import com.example.ligasfragment.model.Equipo
import com.example.ligasfragment.ui.adapter.EquiposAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class FavoritosFragment : Fragment() {

    private lateinit var binding: FragmentFavoritosBinding
    private lateinit var adapter: EquiposAdapter
    private val listaFavoritos = mutableListOf<Equipo>()
    private lateinit var prefs: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefs = requireActivity().getSharedPreferences("mi_preferencia", Context.MODE_PRIVATE)

        adapter = EquiposAdapter(listaFavoritos, prefs)
        binding.recyclerFavoritos.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerFavoritos.adapter = adapter

        cargarFavoritosFirebase()
    }

    private fun cargarFavoritosFirebase() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val ref = FirebaseDatabase.getInstance().getReference("equiposFavoritos").child(userId)
            ref.get().addOnSuccessListener { snapshot ->
                listaFavoritos.clear()
                for (hijo in snapshot.children) {
                    val equipo = hijo.getValue(Equipo::class.java)
                    if (equipo != null) listaFavoritos.add(equipo)
                }
                adapter.notifyDataSetChanged()
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Error al cargar favoritos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
