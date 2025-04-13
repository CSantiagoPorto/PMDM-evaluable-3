package com.example.ligasfragment.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ligasfragment.databinding.FragmentLoginBinding

class LoginFragment: Fragment() {

    private lateinit var binding: FragmentLoginBinding
    //esta variable me dará acceso a todos los elementos de fragmant_login.xml porque
    //FrafmentLogingBinding representa mu layout
    //Esot crea una "puerta" de acceso directo a mi diseño para poder usar sus componentes

    //Método que asocia la parte gráfica con la parte lógica
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?//Me dan el estado guardado
    ): View? {
        //Me interesa retornar una vista
        //Procedemos a darle valor al binding.
        //Infla el diseño fragment_login.xml y guárdalo en la cariable para que pueda usar sus vistas
        binding=FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }
}