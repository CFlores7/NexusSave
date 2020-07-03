package com.example.proyecto

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
import com.example.proyecto.databinding.FragmentGastosBinding
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

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)

            params.setMargins(0, 16, 0, 16)
            params.gravity = Gravity.CENTER

            for (i in 0 until gastosCon.size) {
                val tvCon = TextView(context)
                val tvMon = TextView(context)
                val tvFec = TextView(context)

                tvCon.text = gastosCon[i]
                tvCon.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
                tvCon.setTextColor(getResources().getColor(R.color.colorTextBlack))
                tvCon.layoutParams = params
                listCon?.addView(tvCon)

                tvMon.text = "$" + gastosMon[i]
                tvMon.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
                tvMon.setTextColor(getResources().getColor(R.color.colorRed))
                tvMon.layoutParams = params
                listMon?.addView(tvMon)

                tvFec.text = gastosFec[i]
                tvFec.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
                tvFec.setTextColor(getResources().getColor(R.color.colorTextBlack))
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
}
