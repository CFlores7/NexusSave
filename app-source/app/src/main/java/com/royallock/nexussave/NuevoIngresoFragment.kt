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

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.royallock.nexussave.databinding.FragmentNuevoIngresoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * A simple [Fragment] subclass.
 */
class NuevoIngresoFragment : Fragment() {
    private var mContext: Context? = null

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
            if(TextUtils.isEmpty(binding.etConcepto.text) || TextUtils.isEmpty(binding.etMonto.text)){
                Toast.makeText(mContext, "Debe completar los campos", Toast.LENGTH_LONG).show()
            } else {
                agregarIngreso(binding.etConcepto.text.toString(), binding.etMonto.text.toString())
                activity!!.onBackPressed()
            }
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
        val eliminado = false
        val userID = FirebaseAuth.getInstance().currentUser!!.uid
        val documentReference = FirebaseFirestore.getInstance().collection("users").document(userID)
            .collection("ingresos")
        val calendar = Calendar.getInstance()
        val currentDate = DateFormat.getDateInstance().format(calendar.time)

        val ingreso = HashMap<String, Any>()
        ingreso["concepto"] = concepto
        ingreso["monto"] = monto
        ingreso["fecha"] = currentDate
        ingreso["eliminado"] = eliminado

        documentReference.add(ingreso)

        Toast.makeText(mContext, "Ingreso agregado correctamente", Toast.LENGTH_LONG).show()
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
}
