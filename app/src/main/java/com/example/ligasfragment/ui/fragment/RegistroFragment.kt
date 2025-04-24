package com.example.ligasfragment.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.ligasfragment.R
import com.example.ligasfragment.databinding.FragmentRegistroBinding
import com.example.ligasfragment.model.User
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegistroFragment : Fragment() {

    private lateinit var binding: FragmentRegistroBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onAttach(context: Context) {
        super.onAttach(context)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance("https://ligasfragment-default-rtdb.europe-west1.firebasedatabase.app/")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        binding.btnRegistro.setOnClickListener {
            val correo = binding.editCorreo.text.toString()
            val pass = binding.editPass.text.toString()
            val nombre = binding.editNombre.text.toString()
            val telefono = binding.editTelefono.text.toString()

            if (correo.isBlank() || pass.length < 6 || nombre.isBlank() || telefono.isBlank()) {
                Snackbar.make(binding.root, "Completa todos los campos correctamente", Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(correo, pass).addOnCompleteListener {
                if (it.isSuccessful) {
                    val usuario = User(nombre, telefono)
                    database.reference.child("usuarios").child(auth.currentUser!!.uid).setValue(usuario)
                    findNavController().navigate(R.id.action_registroFragment_to_mainFragment)
                } else {
                    Snackbar.make(binding.root, "Error en el registro", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }
}
