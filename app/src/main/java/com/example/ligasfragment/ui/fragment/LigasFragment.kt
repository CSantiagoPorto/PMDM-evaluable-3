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


class LigasFragment: Fragment() {

    private lateinit var binding: FragmentLigasBinding
    private val listaLigas = ArrayList<Liga>()
    private lateinit var ligaAdapter: LigaAdapter

    // Método que asocia la parte gráfica con la parte lógica
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Me interesa retornar una vista
        binding = FragmentLigasBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Método que se ejecuta al arrancar la vista
    override fun onStart() {
        super.onStart()

        // Configuramos el RecyclerView
        ligaAdapter = LigaAdapter(listaLigas)
        binding.recyclerViewLigas.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewLigas.adapter = ligaAdapter

        // Cargamos las ligas desde la API
        cargarLigasAPI()

        // Botón para volver a la pantalla de login
        binding.buttonVolver.setOnClickListener {
            findNavController().navigate(R.id.action_registroFragment_to_mainFragment)
            //ESTO HAY QUE CAMBIARLE EL ACTION PERO ES PARA QUE NO ROMPA AHORA
        }
    }

    private fun cargarLigasAPI() {
        val url = "https://www.thesportsdb.com/api/v1/json/3/all_leagues.php"
        val colaPeticiones = Volley.newRequestQueue(requireContext())

        val peticion = JsonObjectRequest(
          //  DownloadManager.Request.Method.GET,
            url,
            null, // JSON vacío, ya que solo hacemos GET
            { respuesta ->  // Listener para la respuesta
                val arrayLigas = respuesta.getJSONArray("leagues")
                // Procesamos la respuesta y agregamos las ligas a la lista
                for (i in 0 until arrayLigas.length()) {
                    val liga = arrayLigas.getJSONObject(i)
                    val ligaObj = Liga(
                        liga.getString("strLeague"),  // Nombre de la liga
                        liga.getString("strCountry"),  // País de la liga
                        liga.getString("strLogo")     // Logo de la liga
                    )
                    listaLigas.add(ligaObj)
                }

                // Notificamos al adaptador que los datos han cambiado
                ligaAdapter.notifyDataSetChanged()
            },
            { error ->  // Listener para el error
                Toast.makeText(requireContext(), "Error al cargar las ligas", Toast.LENGTH_SHORT).show()
            }
        )

        colaPeticiones.add(peticion)  // Agrega la petición a la cola de Volley
    }
}