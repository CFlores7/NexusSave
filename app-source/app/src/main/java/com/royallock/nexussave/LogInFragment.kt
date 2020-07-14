package com.royallock.nexussave

/*
    This file is part of Nexus$ave.

    Nexus$ave is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Nexus$ave is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Nexus$ave.  If not, see <https://www.gnu.org/licenses/>.
 */

import android.annotation.SuppressLint
import android.content.Context
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
import com.royallock.nexussave.databinding.FragmentLogInBinding
import com.google.firebase.auth.FirebaseAuth

/**
 * A simple [Fragment] subclass.
 */
class LogInFragment : Fragment() {
    private var mContext: Context? = null

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
            Toast.makeText(mContext, "Ingrese texto en email/contraseña", Toast.LENGTH_LONG).show()
            return
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{
                if(!it.isSuccessful) return@addOnCompleteListener
                val intent = Intent(this.context, ActivityMain::class.java)
                this.activity!!.finish()
                startActivity(intent)
                Toast.makeText(mContext,"¡Bienvenido!", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                Toast.makeText(mContext, "Correo o contraseña incorrecta", Toast.LENGTH_LONG).show()
            }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
}
