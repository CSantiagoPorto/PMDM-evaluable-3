package com.example.ligasfragment.ui.fragment

import android.content.Context
import android.content.SharedPreferences
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
    private lateinit var sharedPreferences: SharedPreferences //Almacena en clave:valor

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLigasBinding.inflate(inflater, container, false)
        //Necesito inicializar sharedPreferences primero
        sharedPreferences = requireActivity().getSharedPreferences("mi_preferencia", Context.MODE_PRIVATE)


        //Necesito configurar el adaptador
        ligaAdapter = LigasAdapter(listaLigas,sharedPreferences)
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

                // Recorremos la lista
                for (i in 0 until arrayLigas.length()) {
                    val liga = arrayLigas.getJSONObject(i)

                    // Si no hay logo
                    val strLogo = try {
                        liga.getString("strLogo")
                    } catch (e: JSONException) {
                        "No logo available" // Valor por defecto si no hay
                    }

                    val ligaObj = Liga(
                        liga.getString("strLeague"), // Nombre de la liga
                        strLogo                       // Logo de la liga
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

    //Ahora necesito implementar la lógica de favoritos
    //Tengo que poder agregarla o ELIMINARLA de favoritos
    //MutableSetOf()-> Crea colecciones que NO PERMITEN DUPLICADOS
    //Si usase List, permitiría duplicados y podría añadir varias veces la misma liga
    private fun marcarFav(liga:Liga){
        val favoritos = sharedPreferences.getStringSet("favoritos", mutableSetOf()) ?: mutableSetOf()
        //aquí intento encontrar las cadanas de texto. Si no encuentra devuelve vacío (mutableSetOf)
        if (favoritos.contains(liga.nombre)) {//Si contiene nombre
            favoritos.remove(liga.nombre)//borra
            Toast.makeText(requireContext(), "${liga.nombre} eliminado de favoritos", Toast.LENGTH_SHORT).show()
        }else{
            favoritos.add(liga.nombre)//Si no contiene es que no está en favoritos, así que lo añade
            Toast.makeText(requireContext(),"${liga.nombre} agregador a favoritos", Toast.LENGTH_SHORT).show()

        }
        sharedPreferences.edit().putStringSet("favoritos",favoritos).apply()

    }
}
