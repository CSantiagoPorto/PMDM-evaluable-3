package com.example.ligasfragment.ui.fragment

import android.app.DownloadManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.ligasfragment.R
import com.example.ligasfragment.databinding.FragmentLigasBinding
import com.example.ligasfragment.databinding.FragmentLoginBinding
import com.example.ligasfragment.databinding.FragmentRegistroBinding
import com.example.ligasfragment.model.Liga
import com.example.ligasfragment.LigasAdapter


import com.android.volley.Request





class LigasFragment : Fragment() {

    private lateinit var binding: FragmentLigasBinding
    private val listaLigas = ArrayList<Liga>()
    private lateinit var ligaAdapter: LigasAdapter

    // Método que asocia la parte gráfica con la parte lógica
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLigasBinding.inflate(inflater, container, false)

        // Configuramos el RecyclerView
        ligaAdapter = LigasAdapter(listaLigas)
        binding.recyclerViewLigas.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewLigas.adapter = ligaAdapter

        // Llamamos a la función para cargar las ligas desde la API
        cargarLigasAPI()

        // Configuramos el botón "Volver"
        binding.buttonVolver.setOnClickListener {
            findNavController().navigate(R.id.action_ligasFragment_to_mainFragment)
        }

        return binding.root
    }

    // Método para cargar las ligas desde la API
    private fun cargarLigasAPI() {
        val url = "https://www.thesportsdb.com/api/v1/json/3/all_leagues.php"
        val colaPeticiones = Volley.newRequestQueue(requireContext())

        val peticion = JsonObjectRequest(
            Request.Method.GET,
            url,
            null, // No se envía JSON, solo GET
            { respuesta ->  // Listener para la respuesta exitosa
                val arrayLigas = respuesta.getJSONArray("leagues")
                // Procesamos las ligas obtenidas
                for (i in 0 until arrayLigas.length()) {
                    val liga = arrayLigas.getJSONObject(i)
                    val ligaObj = Liga(
                        liga.getString("strLeague"),  // Nombre de la liga
                        liga.optString("strCountry", "No country available"),  // País de la liga
                        liga.getString("strLogo")  // Logo de la liga
                    )
                    listaLigas.add(ligaObj)  // Agregamos la liga a la lista
                }
                // Notificamos al adaptador que los datos han cambiado
                ligaAdapter.notifyDataSetChanged()
            },
            { error ->  // Listener para el error
                Toast.makeText(requireContext(), "Error al cargar las ligas", Toast.LENGTH_SHORT).show()
            }
        )

        colaPeticiones.add(peticion)  // Agregamos la petición a la cola de Volley
    }
}