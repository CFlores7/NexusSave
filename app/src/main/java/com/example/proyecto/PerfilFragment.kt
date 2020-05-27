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

/**
 * A simple [Fragment] subclass.
 */
class PerfilFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = ""
        (activity as AppCompatActivity).supportActionBar?.setHomeButtonEnabled(true)

        val binding = DataBindingUtil.inflate<FragmentPerfilBinding>(inflater,
            R.layout.fragment_perfil, container, false)

        setHasOptionsMenu(true)

        binding.btSignOut.setOnClickListener {
            cerrarSesion()
        }

        return binding.root
    }
    private fun cerrarSesion(){
        val intent = Intent(this.context, LoginActivity::class.java)
        FirebaseAuth.getInstance().signOut()
        (activity as AppCompatActivity).finish()
        startActivity(intent)
        Toast.makeText(this.activity,"¡Hasta Pronto!", Toast.LENGTH_LONG).show()
    }
}
