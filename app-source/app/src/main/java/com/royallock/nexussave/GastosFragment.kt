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
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.royallock.nexussave.databinding.FragmentGastosBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

/**
 * A simple [Fragment] subclass.
 */
class GastosFragment : Fragment() {
    private val userID = FirebaseAuth.getInstance().currentUser!!.uid
    private val db = FirebaseFirestore.getInstance()
    private val collectionRef = db.collection("users")
    private val gastosRef = collectionRef.document(userID).collection("gastos")
    private var mContext: Context? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = "GASTOS"
        centerTitle()

        val binding = DataBindingUtil.inflate<FragmentGastosBinding>(inflater,
            R.layout.fragment_gastos, container, false)



        val listCon = binding.llayoutConceptos
        val listMon = binding.llayoutMonto
        val listFec = binding.llayoutFecha

        gastosRef.addSnapshotListener { value, e ->
            if (e != null) {
                return@addSnapshotListener
            }

            val gastosCon = ArrayList<String>()
            val gastosMon = ArrayList<String>()
            val gastosFec = ArrayList<String>()
            for (doc in value!!) {
                if(doc.getBoolean("eliminado") != true) {
                    doc.getString("concepto")?.let {
                        gastosCon.add(it)
                    }
                    doc.getString("monto")?.let {
                        gastosMon.add(it)
                    }
                    doc.getString("fecha")?.let {
                        gastosFec.add(it)
                    }
                }
            }

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)

            params.setMargins(0, 16, 0, 16)
            params.gravity = Gravity.CENTER

            if(listCon.childCount > 0 ){
                listCon.removeAllViews()
                listMon.removeAllViews()
                listFec?.removeAllViews()
            }

            for (i in 0 until gastosCon.size) {
                val tvCon = TextView(mContext)
                val tvMon = TextView(mContext)
                val tvFec = TextView(mContext)

                tvCon.text = gastosCon[i]
                tvCon.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
                tvCon.setTextColor(mContext!!.getResources().getColor(R.color.colorTextBlack))
                tvCon.layoutParams = params

                tvCon.setOnClickListener {
                    val amountTv  = tvCon
                    val amount = amountTv.text.toString()
                    val action = GastosFragmentDirections.actionGastosFragmentToVerGastoFragment(amount)
                    it.findNavController().navigate(action)
                }
                listCon?.addView(tvCon)

                tvMon.text = "$" + gastosMon[i]
                tvMon.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
                tvMon.setTextColor(mContext!!.getResources().getColor(R.color.colorRed))
                tvMon.layoutParams = params
                listMon?.addView(tvMon)

                tvFec.text = gastosFec[i]
                tvFec.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
                tvFec.setTextColor(mContext!!.getResources().getColor(R.color.colorTextBlack))
                tvFec.layoutParams = params
                listFec?.addView(tvFec)
            }

        }

        binding.fabAddGasto.setOnClickListener{
            it.findNavController()
                .navigate(R.id.action_gastosFragment_to_nuevoGastoFragment)
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
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
}
