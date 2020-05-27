package com.example.proyecto

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.proyecto.databinding.FragmentSignUpBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_sign_up.*
import org.w3c.dom.Text
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
            if(binding.etPass.text.toString().equals(binding.etConfPass.text.toString())){
            registrarUsuario(binding.etEmail.text.toString(), binding.etPass.text.toString())
            }else{
                Toast.makeText(this.activity, "Passwords doesn't match", Toast.LENGTH_LONG).show()
            }
        }

        return binding.root
    }
    //Dialogo para elegir fecha
    private fun chooseDate(etFecha: EditText){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(
            activity as AppCompatActivity,
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
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

    private fun registrarUsuario(email: String, password: String){
        if(TextUtils.isEmpty(email)||TextUtils.isEmpty(password)){
            Toast.makeText(this.activity, "Please enter text in email/pass", Toast.LENGTH_LONG).show()
            return
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if(!it.isSuccessful) return@addOnCompleteListener

                Toast.makeText(this.activity, "Usuario creado correctamente", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                Toast.makeText(this.activity, "Error al crear el usuario", Toast.LENGTH_LONG).show()
            }
    }
}
