package com.example.ligasfragment.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.ligasfragment.R
import com.example.ligasfragment.databinding.FragmentMainBinding
import com.example.ligasfragment.model.Liga
import com.example.ligasfragment.ui.fragment.LigaAdapter





class MainFragment: Fragment() {

    private lateinit var binding: FragmentMainBinding
    //esta variable me dará acceso a todos los elementos de fragmant_login.xml porque
    //FrafmentLogingBinding representa mu layout
    //Esot crea una "puerta" de acceso directo a mi diseño para poder usar sus componentes
    private val listaLigas = ArrayList<Liga>()//Lista de ligas del RecyclerView


    //Método que asocia la parte gráfica con la parte lógica
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?//Me dan el estado guardado
    ): View? {
        //Me interesa retornar una vista
        //Procedemos a darle valor al binding.
        //Infla el diseño fragment_login.xml y guárdalo en la cariable para que pueda usar sus vistas
        binding=FragmentMainBinding.inflate(inflater,container,false)
        var adapter=LigaAdapter(listaLigas)
        //Esto va a configurar el RecyclerView para que use el adaptador
        cargarLigasAPI()

        return binding.root
     //   var adapter =LigaAdapter(listaLigas)
    }

    override fun onStart() {
        super.onStart()
        binding.buttonVolver.setOnClickListener(){
            findNavController().navigate(R.id.action_mainFragment_to_loginFragment)
        }
    }


    private fun cargarLigasAPI() {
        val url = "https://www.thesportsdb.com/api/v1/json/3/all_leagues.php"
        val colaPeticiones = Volley.newRequestQueue(requireContext())

        val peticion = JsonObjectRequest(
            Request.Method.GET,
            url,
            null, // JSON vacío, ya que solo hacemos GET
            { respuesta ->  // Listener para la respuesta
                val arrayLigas = respuesta.getJSONArray("leagues")
              //  Log.d("Peticion", "Petición añadida a la cola: $peticion")
                Log.d("API Response", "Respuesta: $respuesta")
                Log.d("API Response", "Ligas obtenidas: $arrayLigas")
                // Agrega las ligas obtenidas a la lista
                for (i in 0 until arrayLigas.length()) {
                    val liga = arrayLigas.getJSONObject(i)
                    val ligaObj = Liga(
                        liga.getString("strLeague"),  // nombre
                        liga.getString("strCountry"),  // pais
                        liga.getString("strLogo")     // logo
                    )
                    listaLigas.add(ligaObj)  // Añadir el objeto liga a la lista
                }
            },
            { error ->  // Listener para el error
                Log.e("API Error", error.toString())
            }
        )
        Log.d("Peticion", "URL: $url")
        Log.d("Peticion", "Petición: $peticion")

        colaPeticiones.add(peticion)  // Agrega la petición a la cola de Volley
    }









}