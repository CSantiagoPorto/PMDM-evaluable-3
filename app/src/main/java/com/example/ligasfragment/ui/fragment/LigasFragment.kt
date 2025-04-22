package com.example.ligasfragment.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.ligasfragment.R
import com.example.ligasfragment.databinding.FragmentLigasBinding

import com.example.ligasfragment.LigasAdapter
import com.example.ligasfragment.model.Liga
import org.json.JSONException

class LigasFragment : Fragment() {

    private lateinit var binding: FragmentLigasBinding
    private val listaLigas = ArrayList<Liga>()
    private lateinit var ligaAdapter: LigasAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLigasBinding.inflate(inflater, container, false)

        ligaAdapter = LigasAdapter(listaLigas)
        binding.recyclerViewLigas.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewLigas.adapter = ligaAdapter

        cargarLigasAPI()

        binding.buttonVolver.setOnClickListener {
            findNavController().navigate(R.id.action_ligasFragment_to_mainFragment)
        }

        return binding.root
    }
    private fun cargarLigasAPI() {
        val url = "https://www.thesportsdb.com/api/v1/json/3/all_leagues.php"
        val colaPeticiones = Volley.newRequestQueue(requireContext())

        val peticion = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            { respuesta ->
                val arrayLigas = respuesta.getJSONArray("leagues")

                // Procesamos las ligas obtenidas
                for (i in 0 until arrayLigas.length()) {
                    val liga = arrayLigas.getJSONObject(i)

                    // Manejo de posibles errores en el campo 'strLogo'
                    val strLogo = try {
                        liga.getString("strLogo")
                    } catch (e: JSONException) {
                        "No logo available" // Valor por defecto si no existe
                    }

                    val ligaObj = Liga(
                        liga.getString("strLeague"), // Nombre de la liga
                        strLogo                       // Logo de la liga (con valor por defecto si no existe)
                    )

                    listaLigas.add(ligaObj)
                }

                // Notificamos que los datos han cambiado
                ligaAdapter.notifyDataSetChanged()
            },
            { error ->
                // Si hay un error en la petición
                Toast.makeText(requireContext(), "Error al cargar las ligas", Toast.LENGTH_SHORT).show()
            }
        )

        colaPeticiones.add(peticion)
    }


}
