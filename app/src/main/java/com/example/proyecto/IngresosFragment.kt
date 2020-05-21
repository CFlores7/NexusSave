package com.example.proyecto

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.proyecto.databinding.FragmentIngresosBinding

/**
 * A simple [Fragment] subclass.
 */
class IngresosFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = "INGRESOS"

        val binding = DataBindingUtil.inflate<FragmentIngresosBinding>(inflater,
            R.layout.fragment_ingresos, container, false)

        binding.fabAddIngreso.setOnClickListener{
            it.findNavController()
                .navigate(R.id.action_ingresosFragment_to_nuevoIngresoFragment)
        }

        return binding.root
    }

}
