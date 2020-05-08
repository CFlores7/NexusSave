package com.example.proyecto

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.proyecto.databinding.FragmentIngresosBinding

/**
 * A simple [Fragment] subclass.
 */
class IngresosFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentIngresosBinding>(inflater,
            R.layout.fragment_ingresos, container, false)


        return binding.root
    }

}
