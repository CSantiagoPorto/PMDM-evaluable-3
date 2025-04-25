package com.example.ligasfragment.ui.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.ligasfragment.R
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
        binding.buttonRegresar.setOnClickListener{
            requireActivity().onBackPressedDispatcher.onBackPressed()
            //Escuchamos al botón y cuando se pulsa simula el comportamiento de atrás en el móvil
            //Sin picar código
        }

        if (ligaNombre.isNotEmpty()) {
            cargarEquiposDeLiga(ligaNombre)
        }

        return binding.root
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true) // Activo menú
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.inicio -> {
                findNavController().navigate(R.id.action_equiposFragment_to_loginFragment)
                true
            }
            R.id.favorito -> {
                findNavController().navigate(R.id.action_equiposFragment_to_favoritosFragment)
                true
            }
            R.id.salir -> {
                requireActivity().finishAffinity()//estp cierra
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
                            val idEquipo=o.getString("idTeam")
                            val nombreEquipos = o.getString("strTeam")
                            val logoEquipos = o.getString("strBadge")
                            val equipo = Equipo(idTeam = idEquipo, name = nombreEquipos, badgeUrl = logoEquipos)
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
