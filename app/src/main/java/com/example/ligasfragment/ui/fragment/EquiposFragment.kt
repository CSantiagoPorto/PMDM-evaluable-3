package com.example.ligasfragment.ui.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.ligasfragment.databinding.FragmentEquiposBinding
import com.example.ligasfragment.model.Equipo
import com.example.ligasfragment.ui.adapter.EquiposAdapter

class EquiposFragment : Fragment() {

    private lateinit var binding: FragmentEquiposBinding
    private lateinit var preferencias: SharedPreferences
    private val listaEquipos = mutableListOf<Equipo>()
    private lateinit var equiposAdapter: EquiposAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEquiposBinding.inflate(inflater, container, false)

        // Obtener el nombre de la liga desde los argumentos del Bundle
        val ligaNombre = arguments?.getString("ligaNombre") ?: ""

        // Inicializar SharedPreferences
        preferencias = requireActivity().getSharedPreferences("mi_preferencia", Context.MODE_PRIVATE)

        // Configurar el RecyclerView para mostrar los equipos
        equiposAdapter = EquiposAdapter(listaEquipos, preferencias)
        binding.recyclerViewEquipos.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = equiposAdapter
        }

        // Cargar los equipos de la liga seleccionada
        if (ligaNombre.isNotEmpty()) {
            cargarEquiposDeLiga(ligaNombre)
        }

        return binding.root
    }
    private fun cargarEquiposDeLiga(ligaNombre: String) {
        val url = "https://www.thesportsdb.com/api/v1/json/3/search_all_teams.php?l=${ligaNombre}"
        Volley.newRequestQueue(requireContext()).add(
            JsonObjectRequest(Request.Method.GET, url, null,
                { resp ->
                    // Comprobamos si la respuesta contiene los equipos
                    val arr = resp.optJSONArray("teams") ?: return@JsonObjectRequest
                    if (arr.length() > 0) {
                        Log.d("EquiposFragment", "Equipos cargados: $arr")  // Aquí vemos si llegan los datos
                        for (i in 0 until arr.length()) {
                            val o = arr.getJSONObject(i)
                            val teamName = o.getString("strTeam")
                            val badgeUrl = o.getString("strBadge")
                            val team = Equipo(name = teamName, badgeUrl = badgeUrl)
                            listaEquipos.add(team)
                        }

                        // Actualizar el RecyclerView con los equipos
                        equiposAdapter.notifyDataSetChanged()
                    } else {
                        Log.d("EquiposFragment", "No se encontraron equipos en la respuesta.")
                    }
                },
                { error ->
                    // Manejo de error
                    Log.e("EquiposFragment", "Error al cargar equipos", error)
                }
            )
        )
    }


}
