package com.example.ligasfragment.ui.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class LigasFragment : Fragment(), LigasAdapter.OnFavoritoClickListener {

    private lateinit var binding: FragmentLigasBinding
    private val listaLigas = mutableListOf<Liga>()
    private lateinit var ligaAdapter: LigasAdapter
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        binding = FragmentLigasBinding.inflate(inflater, container, false)
        prefs = requireActivity().getSharedPreferences("mi_preferencia", Context.MODE_PRIVATE)

        ligaAdapter = LigasAdapter(listaLigas, prefs, this)
        binding.recyclerViewLigas.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ligaAdapter
        }

        binding.buttonVolver.setOnClickListener {
            findNavController().navigate(R.id.action_ligasFragment_to_confirmacionFragment)
        }

        cargarLigasAPI()
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.inicio -> {
                findNavController().navigate(R.id.action_ligasFragment_to_loginFragment)
                true
            }
            R.id.favorito -> {
                findNavController().navigate(R.id.action_ligasFragment_to_favoritosFragment)
                true
            }
            R.id.salir -> {
                requireActivity().finishAffinity() // Esto cierra la app
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun cargarLigasAPI() {
        val url = "https://www.thesportsdb.com/api/v1/json/3/all_leagues.php"
        Volley.newRequestQueue(requireContext()).add(
            JsonObjectRequest(Request.Method.GET, url, null,
                { resp ->
                    val arr = resp.optJSONArray("leagues") ?: return@JsonObjectRequest
                    for (i in 0 until arr.length()) {
                        val o = arr.getJSONObject(i)
                        val id = o.getString("idLeague")
                        val nombre = o.getString("strLeague")
                        val liga = Liga(id = id, nombre = nombre, logoUrl = "")
                        listaLigas.add(liga)
                        ligaAdapter.notifyItemInserted(listaLigas.size - 1)
                       // cargarLogoDeLiga(liga, listaLigas.size - 1)
                    }
                },
                { _ ->
                    Toast.makeText(requireContext(), "Error al cargar las ligas", Toast.LENGTH_SHORT).show()
                }
            )
        )
    }

  /*  private fun cargarLogoDeLiga(liga: Liga, position: Int) {
        val detalleUrl = "https://www.thesportsdb.com/api/v1/json/3/lookupleague.php?badge=1&id=${liga.id}"
        Volley.newRequestQueue(requireContext()).add(
            JsonObjectRequest(Request.Method.GET, detalleUrl, null,
                { resp ->
                    val arr = resp.optJSONArray("seasons")
                    if (arr != null && arr.length() > 0) {
                        val info = arr.getJSONObject(0)
                        val badgeUrl = info.optString("strBadge", "")
                        liga.logoUrl = badgeUrl
                        ligaAdapter.notifyItemChanged(position)
                    }
                },
                { err ->
                    Log.e("LigasFragment", "Error detalle liga ${liga.id}", err)
                }
            )
        )
    }*/
    //Este método está aquí porque leí mal el enunciado y entendí que había que cargar todo

    override fun onLigaClick(liga: Liga) {
        val bundle = Bundle().apply {
            putString("ligaNombre", liga.nombre)
        }
        findNavController().navigate(R.id.action_ligasFragment_to_equiposFragment, bundle)
    }

    override fun onFavoritoClick(liga: Liga, position: Int) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val favoritos = prefs.getStringSet("favoritos", mutableSetOf()) ?: mutableSetOf()
        val set = favoritos.toMutableSet()

        if (!set.add(liga.nombre)) {
            set.remove(liga.nombre)
            Toast.makeText(requireContext(), "${liga.nombre} eliminado de favoritos", Toast.LENGTH_SHORT).show()
            userId?.let {
                val ref = FirebaseDatabase.getInstance().getReference("favoritos").child(it)
                ref.child(liga.nombre).removeValue()
            }
        } else {
            Toast.makeText(requireContext(), "${liga.nombre} agregada a favoritos", Toast.LENGTH_SHORT).show()
            userId?.let {
                val ref = FirebaseDatabase.getInstance().getReference("favoritos").child(it)
                ref.child(liga.nombre).setValue(liga)
            }
        }

        prefs.edit().putStringSet("favoritos", set).apply()
        ligaAdapter.notifyItemChanged(position)
    }
}
