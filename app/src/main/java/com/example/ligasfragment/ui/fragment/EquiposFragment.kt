package com.example.ligasfragment.ui.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        val ligaNombre = arguments?.getString("ligaNombre") ?: ""

        preferencias = requireActivity().getSharedPreferences("mi_preferencia", Context.MODE_PRIVATE)

        equiposAdapter = EquiposAdapter(listaEquipos)
        binding.recyclerViewEquipos.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = equiposAdapter
        }

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
                    val arrayEquipos = resp.optJSONArray("teams") ?: return@JsonObjectRequest
                    if (arrayEquipos.length() > 0) {
                        for (i in 0 until arrayEquipos.length()) {
                            val o = arrayEquipos.getJSONObject(i)
                            val nombreEquipos = o.getString("strTeam")
                            val logoEquipos = o.getString("strBadge")
                            val equipo = Equipo(name = nombreEquipos, badgeUrl = logoEquipos)
                            listaEquipos.add(equipo)
                        }
                        equiposAdapter.notifyDataSetChanged()
                    } else {
                        Log.d("EquiposFragment", "No se encontraron equipos.")
                    }
                },
                { error ->
                    Log.e("EquiposFragment", "Error al cargar equipos", error)
                }
            )
        )
    }
}
