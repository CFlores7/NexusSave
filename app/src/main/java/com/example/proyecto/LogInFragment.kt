package com.example.proyecto

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.proyecto.databinding.FragmentLogInBinding

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
            val intent = Intent(this.context, ActivityMain::class.java)
            (activity as AppCompatActivity).finish()
            startActivity(intent)
        }
        binding.buttonRegistrarse.setOnClickListener {
            it.findNavController()
                .navigate(R.id.action_logInFragment_to_signUpFragment)
        }

        return binding.root
    }
}
