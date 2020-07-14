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
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.royallock.nexussave.databinding.FragmentIngresosBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


/**
 * A simple [Fragment] subclass.
 */
class IngresosFragment : Fragment() {
    private val userID = FirebaseAuth.getInstance().currentUser!!.uid
    private val db = FirebaseFirestore.getInstance()
    private val collectionRef = db.collection("users")
    private val ingresosRef = collectionRef.document(userID).collection("ingresos")
    private val gastosRef = collectionRef.document(userID).collection("gastos")
    private val pagosRef = collectionRef.document(userID).collection("pagos")
    private var mContext: Context? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = "INGRESOS"

        val binding = DataBindingUtil.inflate<FragmentIngresosBinding>(inflater,
            R.layout.fragment_ingresos, container, false)

        val list = binding.linearIngresos
        var total:Double
        var ingresos: Double
        var gastos:Double
        var pagos:Double

        ingresosRef.addSnapshotListener { value, e ->
            if (e != null) {
                return@addSnapshotListener
            }

            val ingresos = ArrayList<String>()
            for (doc in value!!) {
                if (doc.getBoolean("eliminado") != true) {
                    doc.getString("concepto")?.let {
                        ingresos.add(it)
                    }
                }
            }

            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)
            params.setMargins(0, 16, 0, 16)
            params.gravity = Gravity.CENTER

            if(list.childCount > 0 ){
                list.removeAllViews()
            }

            for (i in 0 until ingresos.size) {
                val tv = TextView(mContext)
                tv.text = ingresos[i]
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24F)
                tv.setTextColor(mContext!!.getResources().getColor(R.color.colorTextBlack))
                tv.layoutParams = params

                tv.setOnClickListener {
                    val amountTv  = tv
                    val amount = amountTv.text.toString()
                    val action = IngresosFragmentDirections.actionIngresosFragmentToVerIngresoFragment(amount)
                    it.findNavController().navigate(action)
                }
                list?.addView(tv)
            }

        }

        ingresosRef.addSnapshotListener { value, e ->
            if (e != null) {
                return@addSnapshotListener
            }

            val ingreso = ArrayList<String>()
            for (doc in value!!) {
                doc.getString("monto")?.let {
                    ingreso.add(it)
                }
            }
            ingresos = 0.0
            for (i in 0 until ingreso.size) {
                ingresos += ingreso[i].toFloat()
            }
            gastosRef.addSnapshotListener { value, e ->
                if (e != null) {
                    return@addSnapshotListener
                }

                val gasto = ArrayList<String>()
                for (doc in value!!) {
                    doc.getString("monto")?.let {
                        gasto.add(it)
                    }
                }
                gastos = 0.0
                for (i in 0 until gasto.size) {
                    gastos += gasto[i].toFloat()
                }
                pagosRef.addSnapshotListener { value, e ->
                    if (e != null) {
                        return@addSnapshotListener
                    }


                    val pago = ArrayList<String>()
                    for (doc in value!!) {
                        if(doc.getString("estado").equals("CANCELADO")) {
                            doc.getString("monto")?.let {
                                pago.add(it)
                            }
                        }
                    }
                    pagos = 0.0
                    for (i in 0 until pago.size) {
                        pagos += pago[i].toFloat()
                    }
                    total = ingresos - gastos - pagos
                    binding.tvDinero.text = "%.2f".format(total)
                }
            }
        }

        binding.fabAddIngreso.setOnClickListener{
            it.findNavController()
                .navigate(R.id.action_ingresosFragment_to_nuevoIngresoFragment)
        }

        return binding.root
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
}
