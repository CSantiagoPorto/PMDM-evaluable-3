package com.example.ligasfragment.ui.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ligasfragment.databinding.FragmentFavoritosBinding
import com.example.ligasfragment.model.Equipo
import com.example.ligasfragment.ui.adapter.FavoritosAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class FavoritosFragment : Fragment() {

    private lateinit var binding: FragmentFavoritosBinding
    private lateinit var favoritosAdapter: FavoritosAdapter
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


        favoritosAdapter = FavoritosAdapter(listaFavoritos)

        binding.recyclerFavoritos.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = favoritosAdapter

        }

        binding.btnVolverFavoritos.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // Cargar equipos favoritos desde Firebase
        cargarFavoritosFirebase()
    }

     private fun cargarFavoritosFirebase(){
         val userId = FirebaseAuth.getInstance().currentUser?.uid
         val favoritos = FirebaseDatabase.getInstance("https://ligasfragment-default-rtdb.europe-west1.firebasedatabase.app/").reference.child("usuarios").child(userId ?: "").child("listaFav")

         favoritos.get().addOnSuccessListener { snapshot->
             for(child in snapshot.children){
                 val equipo =child.getValue(Equipo::class.java)
                 Log.v("equiposAdapter","equipo polsado")
                 if (equipo!=null){
                     listaFavoritos.add(equipo)

                 }

             }
             favoritosAdapter.notifyDataSetChanged()
         }
     }
}
