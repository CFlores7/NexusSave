package com.example.proyecto

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.proyecto.databinding.FragmentPerfilBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
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
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        (activity as AppCompatActivity).supportActionBar?.setHomeButtonEnabled(true)

        val binding = DataBindingUtil.inflate<FragmentPerfilBinding>(inflater,
            R.layout.fragment_perfil, container, false)

        loadUser(binding)

        setHasOptionsMenu(true)

        binding.btSignOut.setOnClickListener {
            cerrarSesion()
        }

        return binding.root
    }

    private fun loadUser(binding: FragmentPerfilBinding){

        documentReference.addSnapshotListener { snapshot, e ->
            if (e != null) {
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                binding.tvNombre.text = snapshot.getString("fName")!!
                binding.tvUbicacion.text = (snapshot.getString("ciudad")!! + ", " + snapshot.getString("pais")!!)
                binding.tvFechaNaci.text = snapshot.getString("birth")!!
                binding.tvCorreo.text = snapshot.getString("email")!!
            } else {
                Log.d(TAG, "No Cambio")
            }

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
