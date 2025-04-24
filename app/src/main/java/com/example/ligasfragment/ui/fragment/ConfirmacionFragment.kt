package com.example.ligasfragment.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.ligasfragment.R
import com.example.ligasfragment.databinding.FragmentMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ConfirmacionFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
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
        binding = FragmentMainBinding.inflate(inflater, container, false)

        // Configuración del botón "Seguir Navegando"
        binding.buttonSeguirNavegando.setOnClickListener {
            // Navegar a LigasFragment
            findNavController().navigate(R.id.action_mainFragment_to_ligasFragment)
            //coger referencia y nombre

                ///Añadir campo Ligas para que se muestre
                //binding.textoUsuario.text=it.value.toString()
            database.reference
                .child("app")
                .child("nombre")
                .addChildEventListener(object : ChildEventListener {
                    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                        Log.v("datos", "Hijo añadido: ${snapshot.key} => ${snapshot.value}")
                    }
                    override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                        Log.v("datos", "Hijo cambiado: ${snapshot.key} => ${snapshot.value}")
                    }
                    override fun onChildRemoved(snapshot: DataSnapshot) { /*…*/ }
                    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) { /*…*/ }
                    override fun onCancelled(error: DatabaseError) {
                        Log.e("datos", "Error en ChildEventListener", error.toException())
                    }
                })


        }

        // Configuración del botón "Volver"
        binding.buttonVolver.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_loginFragment)
        }

        return binding.root
    }
}

