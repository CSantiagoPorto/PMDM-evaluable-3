package com.example.ligasfragment.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.ligasfragment.databinding.FragmentMainBinding
import com.example.ligasfragment.model.Liga
import com.example.ligasfragment.ui.adapter.LigaAdapter





class MainFragment: Fragment() {

    private lateinit var binding: FragmentMainBinding
    //esta variable me dará acceso a todos los elementos de fragmant_login.xml porque
    //FrafmentLogingBinding representa mu layout
    //Esot crea una "puerta" de acceso directo a mi diseño para poder usar sus componentes
    private val listaLigas = ArrayList<Liga>()


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
        var adapter=com.example.ligasfragment.ui.fragment.LigaAdapter(listaLigas)
        //Esto va a configurar el RecyclerView para que use el adaptador
        return binding.root
     //   var adapter =LigaAdapter(listaLigas)
    }



   /* private fun cargarLigasAPI(){
        val url= "https://www.thesportsdb.com/api/v1/json/3/all_leagues.php"
        val colaPeticiones = Volley.newRequestQueue(requireContext())
        //Necesito una cola de peticiones para nuestros amigos GET y POST
        val peticion= JsonObjectRequest(Request.Method.GET,//le indico método
            url,//le doy la url
            null,//de primeras vacío, por tanto null
            {respuesta->
                val arrayLigas=respuesta.getJSONObject(i)

            })


    }*/
}