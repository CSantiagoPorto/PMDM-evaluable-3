package com.example.ligasfragment.ui.fragment

import android.content.Context
import android.os.Bundle
import android.renderscript.ScriptGroup.Binding
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.ligasfragment.R
import com.example.ligasfragment.databinding.FragmentLoginBinding
import com.example.ligasfragment.model.User
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class LoginFragment: Fragment() {

    private lateinit var binding: FragmentLoginBinding
    //esta variable me dará acceso a todos los elementos de fragmant_login.xml porque
    //FrafmentLogingBinding representa mu layout
    //Esot crea una "puerta" de acceso directo a mi diseño para poder usar sus componentes
    private lateinit var auth: FirebaseAuth


    override fun onAttach(context: Context) {
        super.onAttach(context)
        auth=FirebaseAuth.getInstance()
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
        binding=FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.buttonLogin.setOnClickListener(){
            auth.signInWithEmailAndPassword(
                binding.editTextEmail.text.toString(),
                binding.editTextPassword.text.toString()
            ).addOnCompleteListener{
                if(it.isSuccessful){
                    auth.currentUser!!.uid
                    val bundle=Bundle()
                    val usuario= User(binding.editTextEmail.text.toString(), binding.editTextPassword.text.toString())
                    bundle.putSerializable("usuario",usuario)
                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment, bundle)
                }else{
                    Snackbar.make(binding.root,"Error de registro",Snackbar.LENGTH_SHORT).setAction("Quieres registrarte?"){findNavController().navigate(R.id.action_loginFragment_to_registroFragment)}
                        .show()
                }
            }

           //


        }

    }
}