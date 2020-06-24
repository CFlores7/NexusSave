package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.proyecto.databinding.FragmentPerfilBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

/**
 * A simple [Fragment] subclass.
 */
class PerfilFragment : Fragment() {
    private val userID = FirebaseAuth.getInstance().currentUser!!.uid
    private val db = FirebaseFirestore.getInstance()
    private val documentReference = db.collection("users").document(userID)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = ""
        (activity as AppCompatActivity).supportActionBar?.setHomeButtonEnabled(true)

        val binding = DataBindingUtil.inflate<FragmentPerfilBinding>(inflater,
            R.layout.fragment_perfil, container, false)

        loadUserData(binding)

        setHasOptionsMenu(true)

        binding.btSignOut.setOnClickListener {
            cerrarSesion()
        }

        return binding.root
    }
    private fun loadUserData(binding: FragmentPerfilBinding) {
        var nombre: String
        var ciudad: String
        var pais: String
        var fechaNac: String
        var email: String

        documentReference.get()
            .addOnSuccessListener {
                if(it.exists()){
                    nombre = it.getString("fName")!!
                    ciudad = it.getString("ciudad")!!
                    pais = it.getString("pais")!!
                    fechaNac = it.getString("birth")!!
                    email = it.getString("email")!!

                    binding.tvNombre.text = nombre
                    binding.tvUbicacion.text = (ciudad + ", " + pais)
                    binding.tvFechaNaci.text = fechaNac
                    binding.tvCorreo.text = email
                }
            }
            .addOnFailureListener{
                Toast.makeText(this.activity, "Error!", Toast.LENGTH_SHORT).show()
            }
    }

    private fun cerrarSesion(){
        val intent = Intent(this.context, LoginActivity::class.java)
        FirebaseAuth.getInstance().signOut()
        (activity as AppCompatActivity).finish()
        startActivity(intent)
        Toast.makeText(this.activity,"¡Hasta Pronto!", Toast.LENGTH_LONG).show()
    }
}
