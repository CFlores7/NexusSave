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
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.royallock.nexussave.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.HashMap

/**
 * A simple [Fragment] subclass.
 */
class SignUpFragment : Fragment() {
    private var mContext: Context? = null

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
            if(binding.etPass.length()<8){
                Toast.makeText(mContext, "Contraseña debe tener mas de 8 caracteres",
                    Toast.LENGTH_LONG).show()
            }
            if(binding.etPass.text.toString().equals(binding.etConfPass.text.toString())){
            registrarUsuario(binding.etNombre.text.toString(), binding.etEmail.text.toString(),
                            binding.etPass.text.toString(), binding.etCiudad.text.toString(),
                    binding.spPais.selectedItem.toString(), binding.etFechaNaci.text.toString())
            }else{
                Toast.makeText(mContext, "Contraseñas no coinciden", Toast.LENGTH_LONG).show()
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

    private fun registrarUsuario(fullname:String, email: String, password: String, ciudad: String, pais: String, fechaNac: String){
        if(TextUtils.isEmpty(email)||TextUtils.isEmpty(password)){
            Toast.makeText(mContext, "Ingrese texto en email/contraseña", Toast.LENGTH_LONG).show()
            return
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if(!it.isSuccessful) return@addOnCompleteListener
                val userID = FirebaseAuth.getInstance().currentUser!!.uid
                val documentReference = FirebaseFirestore.getInstance().collection("users").document(userID)

                val user = HashMap<String, Any>()
                user["fName"] = fullname
                user["email"] = email
                user["ciudad"] = ciudad
                user["pais"] = pais
                user["birth"] = fechaNac
                documentReference.set(user)
                Toast.makeText(mContext, "Usuario creado correctamente", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                Toast.makeText(mContext, "Error al crear el usuario", Toast.LENGTH_LONG).show()
            }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
}
