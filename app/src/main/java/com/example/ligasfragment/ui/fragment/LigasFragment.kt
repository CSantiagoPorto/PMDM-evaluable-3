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


class LigasFragment : Fragment() {

    private lateinit var binding: FragmentLigasBinding
    private val listaLigas = ArrayList<Liga>()

    // Método que asocia la parte gráfica con la parte lógica
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflamos el diseño y asignamos el binding
        binding = FragmentLigasBinding.inflate(inflater, container, false)

        // Configuramos el RecyclerView
        val recyclerView = binding.recyclerViewLigas
        recyclerView.layoutManager = LinearLayoutManager(context) // Establece el LayoutManager
        val adapter = LigasAdapter(listaLigas)
        recyclerView.adapter = adapter  // Establece el adaptador

        // Llamamos a la función para cargar las ligas (desde la API o de una fuente local)
        cargarLigas()

        return binding.root
    }

    // Función para cargar las ligas (puedes agregar aquí tu lógica de carga)
    private fun cargarLigas() {
        // Aquí deberías obtener las ligas (de la API o localmente)
        listaLigas.add(Liga("Liga 1", "País 1", "Logo 1"))
        listaLigas.add(Liga("Liga 2", "País 2", "Logo 2"))
        // Agrega más ligas...

        // Notifica al adaptador que los datos han cambiado
        binding.recyclerViewLigas.adapter?.notifyDataSetChanged()
    }
}