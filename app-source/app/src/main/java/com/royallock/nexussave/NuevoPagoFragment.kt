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

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.royallock.nexussave.databinding.FragmentNuevoPagoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class NuevoPagoFragment : Fragment() {
    private val userID = FirebaseAuth.getInstance().currentUser!!.uid
    private val db = FirebaseFirestore.getInstance()
    private val collectionRef = db.collection("users")
    private val pagosRef = collectionRef.document(userID).collection("pagos")
    private var mContext: Context? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = "NUEVO PAGO"

        val binding = DataBindingUtil.inflate<FragmentNuevoPagoBinding>(inflater,
            R.layout.fragment_nuevo_pago, container, false)

        centerTitle()

        binding.btFecha.setOnClickListener {
            chooseDate(binding.btFecha, binding.etFecha)
        }

        binding.btCancelar.setOnClickListener {
            activity!!.onBackPressed()
        }

        binding.btAgregar.setOnClickListener {
            if(TextUtils.isEmpty(binding.etConcepto.text) || TextUtils.isEmpty(binding.etMonto.text)
                ||TextUtils.isEmpty(binding.etFecha.text)){
                Toast.makeText(mContext, "Debe completar los campos", Toast.LENGTH_LONG).show()
            } else {
                agregarPago(
                    binding.etConcepto.text.toString(), binding.etMonto.text.toString(),
                    binding.etFecha.text.toString()
                )

                activity!!.onBackPressed()
            }
        }

        return binding.root
    }
    //Dialogo para elegir fecha
    private fun chooseDate(btFecha: Button, etFecha: EditText){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(
            activity as AppCompatActivity,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                val mes = monthOfYear + 1
                // Display Selected date in textbox
                etFecha.setText("" + dayOfMonth + "/" + mes + "/" + year)
            },
            year,
            month,
            day
        )
        dpd.datePicker.minDate = System.currentTimeMillis()
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

    private fun agregarPago(concepto: String, monto: String, fecha: String){
        val estado = "PENDIENTE"
        val eliminado = false
        val userID = FirebaseAuth.getInstance().currentUser!!.uid
        val documentReference = FirebaseFirestore.getInstance().collection("users").document(userID)
            .collection("pagos")

        val pago = HashMap<String, Any>()
        pago["concepto"] = concepto
        pago["monto"] = monto
        pago["fecha"] = fecha
        pago["estado"] = estado
        pago["eliminado"] = eliminado

        documentReference.add(pago)

        Toast.makeText(mContext, "Pago agregado correctamente", Toast.LENGTH_LONG).show()
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
}
