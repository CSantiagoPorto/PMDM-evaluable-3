package com.example.ligasfragment.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.ligasfragment.R
import com.example.ligasfragment.databinding.FragmentLoginBinding
import com.example.ligasfragment.databinding.FragmentRegistroBinding
import com.google.firebase.auth.FirebaseAuth

class RegistroFragment: Fragment() {

    private lateinit var binding: FragmentRegistroBinding

    //esta variable me dará acceso a todos los elementos de fragmant_login.xml porque
    //FrafmentLogingBinding representa mu layout
    //Esot crea una "puerta" de acceso directo a mi diseño para poder usar sus componentes




    private lateinit var auth:FirebaseAuth
    override fun onAttach(context: Context) {
        super.onAttach(context)
        auth= FirebaseAuth.getInstance()//Ya puedo autenticar
    }


    //Método que asocia la parte gráfica con la parte lógica
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?//Me dan el estado guardado
    ): View? {
        //Me interesa retornar una vista
        //Procedemos a darle valor al binding.
        //Infla el diseño fragment_login.xml y guárdalo en la cariable para que pueda usar sus vistas
        binding=FragmentRegistroBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.buttonRegistro.setOnClickListener(){
            auth.createUserWithEmailAndPassword(
                binding.editTextEmailRegistro.text.toString(),binding.editTextPasswordRegistro.text.toString()
                //Acabo de crear un usuario
            )

            //findNavController().navigate(R.id.action_registroFragment_to_mainFragment)
        }

    }
}