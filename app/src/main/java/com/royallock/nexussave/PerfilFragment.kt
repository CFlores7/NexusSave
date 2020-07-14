package com.example.nexussave

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.nexussave.databinding.FragmentPerfilBinding
import com.google.firebase.auth.FirebaseAuth
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
        //Setting Title
        (activity as AppCompatActivity).supportActionBar?.title = ""
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
