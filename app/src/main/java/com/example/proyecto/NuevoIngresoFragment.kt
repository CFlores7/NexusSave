package com.example.proyecto

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.proyecto.databinding.FragmentNuevoIngresoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * A simple [Fragment] subclass.
 */
class NuevoIngresoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = "NUEVO INGRESO"

        val binding = DataBindingUtil.inflate<FragmentNuevoIngresoBinding>(inflater,
            R.layout.fragment_nuevo_ingreso, container, false)

        centerTitle()

        binding.btCancelar.setOnClickListener {
            activity!!.onBackPressed()
        }

        binding.btAgregar.setOnClickListener {
            agregarIngreso(binding.etConcepto.text.toString(), binding.etMonto.text.toString())
            activity!!.onBackPressed()
        }

        return binding.root
    }
    //Centrar texto en ActionBar
    private fun centerTitle() {
        val textViews = ArrayList<View>()
        activity?.window?.decorView?.findViewsWithText(textViews, activity?.title, View.FIND_VIEWS_WITH_TEXT)
        if (textViews.size > 0) {
            var appCompatTextView: AppCompatTextView? = null
            if (textViews.size == 1)
                appCompatTextView = textViews[0] as AppCompatTextView
            else {
                for (v in textViews) {
                    if (v.parent is Toolbar) {
                        appCompatTextView = v as AppCompatTextView
                        break
                    }
                }
            }
            if (appCompatTextView != null) {
                val params = appCompatTextView.layoutParams
                params.width = ViewGroup.LayoutParams.MATCH_PARENT
                appCompatTextView.layoutParams = params
                appCompatTextView.textAlignment = View.TEXT_ALIGNMENT_CENTER
            }
        }
    }

    private fun agregarIngreso(concepto: String, monto: String){
        val userID = FirebaseAuth.getInstance().currentUser!!.uid
        val documentReference = FirebaseFirestore.getInstance().collection("users").document(userID)
            .collection("ingresos")
        val calendar = Calendar.getInstance()
        val currentDate = DateFormat.getDateInstance().format(calendar.time)

        val ingreso = HashMap<String, Any>()
        ingreso["concepto"] = concepto
        ingreso["monto"] = monto
        ingreso["fecha"] = currentDate

        documentReference.add(ingreso)

        Toast.makeText(this.activity, "Ingreso agregado correctamente", Toast.LENGTH_LONG).show()
    }
}
