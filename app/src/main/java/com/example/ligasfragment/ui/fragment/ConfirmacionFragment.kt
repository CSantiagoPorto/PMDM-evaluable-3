package com.example.ligasfragment.ui.fragment

import android.content.Context
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
import com.example.ligasfragment.R
import com.example.ligasfragment.databinding.FragmentConfirmacionBinding

import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.database.FirebaseDatabase


class ConfirmacionFragment : Fragment() {

    private lateinit var binding: FragmentConfirmacionBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var  database: FirebaseDatabase
    override fun onAttach(context: Context) {
        super.onAttach(context)
        auth=FirebaseAuth.getInstance()//Tengo el uid de usuario aquí
        //Ahora ya tengo el usuario en toda la app
        database=FirebaseDatabase.getInstance("https://ligasfragment-default-rtdb.europe-west1.firebasedatabase.app/")
    }

    // Método que asocia la parte gráfica con la parte lógica
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConfirmacionBinding.inflate(inflater, container, false)
        val userId=auth.currentUser!!.uid
        if(userId!=null){
            val refUser=database.reference.child("usuarios").child(userId)
            refUser.get().addOnSuccessListener { snapshot ->
                val nombre= snapshot.child("nombre").getValue(String ::class.java)
                if(!nombre.isNullOrEmpty()){
                    binding.textUsuario.text= "Sesión iniciada como $nombre"
                }
            }.addOnFailureListener{binding.textUsuario.text= "Sesion iniciada"}
        }

        // Configuración del botón "Seguir Navegando"
        binding.buttonSeguirNavegando.setOnClickListener {
            // Navego a LigasFragment
            findNavController().navigate(R.id.action_confirmacionFragment_to_ligasFragment)
            //coger referencia y nombre






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
                findNavController().navigate(R.id.action_confirmacionFragment_to_loginFragment)
                true
            }
            R.id.favorito -> {
                findNavController().navigate(R.id.action_confirmacionFragment_to_favoritosFragment)
                true
            }
            R.id.salir -> {
                requireActivity().finishAffinity()//estp cierra
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}

