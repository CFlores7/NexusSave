package com.example.proyecto

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.proyecto.databinding.FragmentLogInBinding
import com.google.firebase.auth.FirebaseAuth

/**
 * A simple [Fragment] subclass.
 */
class LogInFragment : Fragment() {

    @SuppressLint("RestrictedApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Ocultando ActionBar
        (activity as AppCompatActivity).supportActionBar?.hide()
        (activity as AppCompatActivity).supportActionBar?.setShowHideAnimationEnabled(false)

        val binding = DataBindingUtil.inflate<FragmentLogInBinding>(inflater,
            R.layout.fragment_log_in, container, false)

        binding.buttonIngresar.setOnClickListener {
            ingresar(binding.etEmail.text.toString(), binding.etPass.text.toString())
        }
        binding.buttonRegistrarse.setOnClickListener {
            it.findNavController()
                .navigate(R.id.action_logInFragment_to_signUpFragment)
        }
        return binding.root
    }
    private fun ingresar(email: String, password: String){
        if(TextUtils.isEmpty(email)|| TextUtils.isEmpty(password)){
            Toast.makeText(this.activity, "Please enter text in email/pass", Toast.LENGTH_LONG).show()
            return
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{
                if(!it.isSuccessful) return@addOnCompleteListener
                val intent = Intent(this.context, ActivityMain::class.java)
                this.activity!!.finish()
                startActivity(intent)
                Toast.makeText(this.activity,"¡Bienvenido!", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                Toast.makeText(this.activity, "Correo o contraseña incorrecta", Toast.LENGTH_LONG).show()
            }
    }
}
