package com.example.proyecto

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.proyecto.databinding.FragmentPerfilBinding

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

        return binding.root
    }
}
