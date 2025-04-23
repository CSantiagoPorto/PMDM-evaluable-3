package com.example.ligasfragment.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.ligasfragment.R
import com.example.ligasfragment.databinding.FragmentMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var auth: FirebaseAuth
    override fun onAttach(context: Context) {
        super.onAttach(context)
        auth=FirebaseAuth.getInstance()//Tengo el uid de usuario aquí
        //Ahora ya tengo el usuario en toda la app
    }

    // Método que asocia la parte gráfica con la parte lógica
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)

        // Configuración del botón "Seguir Navegando"
        binding.buttonSeguirNavegando.setOnClickListener {
            // Navegar a LigasFragment
            findNavController().navigate(R.id.action_mainFragment_to_ligasFragment)
        }

        // Configuración del botón "Volver"
        binding.buttonVolver.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_loginFragment)
        }

        return binding.root
    }
}

