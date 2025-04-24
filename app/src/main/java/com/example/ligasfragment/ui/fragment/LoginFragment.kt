package com.example.ligasfragment.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.ligasfragment.R
import com.example.ligasfragment.databinding.FragmentLoginBinding
import com.example.ligasfragment.model.User
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
        (requireActivity() as AppCompatActivity).invalidateOptionsMenu()

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(false)
        (requireActivity() as AppCompatActivity).invalidateOptionsMenu()
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        return binding.root
    }
    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.clear()
        super.onPrepareOptionsMenu(menu)
    }

    override fun onStart() {
        super.onStart()

        // LOGIN
        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()

            if (email.isBlank() || password.isBlank()) {
                Snackbar.make(binding.root, "¿Quieres registrarte?", Snackbar.LENGTH_LONG)
                    .setAction("Sí") {
                        findNavController().navigate(R.id.action_loginFragment_to_registroFragment)
                    }.show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val bundle = Bundle()
                        val usuario = User(email, password)
                        bundle.putSerializable("usuario", usuario)
                        (requireActivity() as AppCompatActivity).supportActionBar?.show()
                        findNavController().navigate(R.id.action_loginFragment_to_mainFragment, bundle)
                    } else {
                        Snackbar.make(binding.root, "Credenciales incorrectas", Snackbar.LENGTH_SHORT)
                            .setAction("¿Quieres registrarte?") {
                                findNavController().navigate(R.id.action_loginFragment_to_registroFragment)
                            }.show()
                    }
                }
        }

        // REGISTRO
        binding.buttonRegistro.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registroFragment)
        }
    }
}
