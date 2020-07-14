package com.royallock.nexussave

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.royallock.nexussave.databinding.FragmentNuevoGastoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class NuevoGastoFragment : Fragment() {
    private var mContext: Context? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = "NUEVO GASTO"

        val binding = DataBindingUtil.inflate<FragmentNuevoGastoBinding>(inflater,
            R.layout.fragment_nuevo_gasto, container, false)

        centerTitle()

        Toast.makeText(this.activity,
            "El gasto no puede registrarse si es anterior a 3 dias.",
            Toast.LENGTH_LONG).show()

        binding.btFecha.setOnClickListener {
            chooseDate(binding.etFecha)
        }

        binding.btCancelar.setOnClickListener {
            activity!!.onBackPressed()
        }

        binding.btAgregar.setOnClickListener {
            if(TextUtils.isEmpty(binding.etConcepto.text)|| TextUtils.isEmpty(binding.etMonto.text)
                || TextUtils.isEmpty(binding.etFecha.text)){
                Toast.makeText(mContext, "Debe completar los campos", Toast.LENGTH_LONG).show()
            } else {
                agregarGasto(
                    binding.etConcepto.text.toString(), binding.etMonto.text.toString(),
                    binding.etFecha.text.toString()
                )
                activity!!.onBackPressed()
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
        val minDay = 3*24*60*60*1000

        val dpd = DatePickerDialog(
            activity as AppCompatActivity, android.R.style.Theme_Material_Dialog_MinWidth,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                val mes = monthOfYear + 1
                // Display Selected date in textbox
                etFecha.setText("" + dayOfMonth + "/" + mes + "/" + year)
            },
            year,
            month,
            day
        )
        dpd.datePicker.minDate = (System.currentTimeMillis() - minDay)
        dpd.datePicker.maxDate = System.currentTimeMillis()
        dpd.show()
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

    private fun agregarGasto(concepto: String, monto: String, fecha: String){
        val eliminado = false
        val userID = FirebaseAuth.getInstance().currentUser!!.uid
        val documentReference = FirebaseFirestore.getInstance().collection("users").document(userID)
            .collection("gastos")

        val gasto = HashMap<String, Any>()
        gasto["concepto"] = concepto
        gasto["monto"] = monto
        gasto["fecha"] = fecha
        gasto["eliminado"] = eliminado

        documentReference.add(gasto)

        Toast.makeText(mContext, "Gasto agregado correctamente", Toast.LENGTH_LONG).show()
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
}
