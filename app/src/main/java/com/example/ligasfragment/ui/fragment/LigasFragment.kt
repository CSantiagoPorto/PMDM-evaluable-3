// LigasFragment.kt
package com.example.ligasfragment.ui.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.ligasfragment.LigasAdapter
import com.example.ligasfragment.R
import com.example.ligasfragment.databinding.FragmentLigasBinding
import com.example.ligasfragment.model.Liga

class LigasFragment : Fragment(), LigasAdapter.OnFavoritoClickListener {

    private lateinit var binding: FragmentLigasBinding
    private val listaLigas = mutableListOf<Liga>()
    private lateinit var ligaAdapter: LigasAdapter
    private lateinit var prefs: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLigasBinding.inflate(inflater, container, false)

        // 1) Inicializamos SharedPreferences
        prefs = requireActivity()
            .getSharedPreferences("mi_preferencia", Context.MODE_PRIVATE)

        // 2) Creamos el adapter y se lo asignamos al RecyclerView
        ligaAdapter = LigasAdapter(listaLigas, prefs, this)
        binding.recyclerViewLigas.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ligaAdapter
        }

        // 3) Botón "Volver"
        binding.buttonVolver.setOnClickListener {
            findNavController().navigate(R.id.action_ligasFragment_to_mainFragment)
        }

        // 4) Arrancamos la carga inicial
        cargarLigasAPI()

        return binding.root
    }

    private fun cargarLigasAPI() {
        val url = "https://www.thesportsdb.com/api/v1/json/3/all_leagues.php"
        Volley.newRequestQueue(requireContext()).add(
            JsonObjectRequest(Request.Method.GET, url, null,
                { resp ->
                    val arr = resp.optJSONArray("leagues") ?: return@JsonObjectRequest
                    for (i in 0 until arr.length()) {
                        val o = arr.getJSONObject(i)
                        val id      = o.getString("idLeague")
                        val nombre  = o.getString("strLeague")
                        // Logo provisional vacío
                        val liga = Liga(id = id, nombre = nombre, logoUrl = "")
                        listaLigas.add(liga)
                        // Avisamos de la inserción para que pinte el nombre
                        ligaAdapter.notifyItemInserted(listaLigas.size - 1)
                        // Y lanzamos la petición para cargar el logo real
                        cargarLogoDeLiga(liga, listaLigas.size - 1)
                    }
                },
                { _ ->
                    Toast.makeText(requireContext(), "Error al cargar las ligas", Toast.LENGTH_SHORT).show()
                }
            )
        )
    }

    private fun cargarLogoDeLiga(liga: Liga, position: Int) {
        val detalleUrl = "https://www.thesportsdb.com/api/v1/json/3/lookupleague.php?id=${liga.id}"
        Volley.newRequestQueue(requireContext()).add(
            JsonObjectRequest(Request.Method.GET, detalleUrl, null,
                { resp ->
                    val arr = resp.optJSONArray("leagues")
                    if (arr != null && arr.length() > 0) {
                        val info     = arr.getJSONObject(0)
                        val badgeUrl = info.optString("strBadge", "")
                        liga.logoUrl = badgeUrl
                        // Refresca SOLO esa fila para que muestre el logo
                        ligaAdapter.notifyItemChanged(position)
                    }
                },
                { err ->
                    Log.e("LigasFragment", "Error detalle liga ${liga.id}", err)
                }
            )
        )
    }

    // Este método se llama desde el Adapter cuando pulsas la estrella
    override fun onFavoritoClick(liga: Liga, position: Int) {
        // toggle en SharedPreferences
        val set = prefs.getStringSet("favoritos", mutableSetOf())!!
            .toMutableSet()
        if (!set.add(liga.nombre)) {
            set.remove(liga.nombre)
            Toast.makeText(requireContext(), "${liga.nombre} eliminado de favoritos", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "${liga.nombre} agregado a favoritos", Toast.LENGTH_SHORT).show()
        }
        prefs.edit().putStringSet("favoritos", set).apply()
        // Refresca SOLO esa fila para cambiar el icono
        ligaAdapter.notifyItemChanged(position)
    }
}
