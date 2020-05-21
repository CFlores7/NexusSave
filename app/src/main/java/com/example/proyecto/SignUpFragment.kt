package com.example.proyecto

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.proyecto.databinding.FragmentSignUpBinding
import kotlinx.android.synthetic.main.fragment_sign_up.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class SignUpFragment : Fragment() {

    @SuppressLint("RestrictedApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Ocultando ActionBar
        (activity as AppCompatActivity).supportActionBar?.hide()
        (activity as AppCompatActivity).supportActionBar?.setShowHideAnimationEnabled(false)

        val binding = DataBindingUtil.inflate<FragmentSignUpBinding>(inflater,
            R.layout.fragment_sign_up, container, false)

        //Pais Seleccionado Default -> El Salvador
        binding.spPais.setSelection(58)

        binding.etFechaNaci.isFocusable = false
        binding.etFechaNaci.setOnClickListener {
            chooseDate(binding.etFechaNaci)
        }

        binding.btnCancelar.setOnClickListener {
            it.findNavController().navigate(R.id.action_signUpFragment_to_logInFragment)
        }
        binding.btnCrear.setOnClickListener {
            //it.findNavController().navigate(R.id.action_signUpFragment_to_logInFragment)
        }

        return binding.root
    }
    //Dialogo para elegir fecha
    private fun chooseDate(etFecha: EditText){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val minDay = 3*24*60*60*1000

        val dpd = DatePickerDialog(
            activity as AppCompatActivity,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                val mes = monthOfYear+1
                // Display Selected date in textbox
                etFecha.setText("" + dayOfMonth + "/" + mes + "/" + year)
            },
            year,
            month,
            day
        )
        dpd.datePicker.maxDate = System.currentTimeMillis()
        dpd.show()
    }
}
