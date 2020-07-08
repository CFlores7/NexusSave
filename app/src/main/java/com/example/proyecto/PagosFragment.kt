package com.example.proyecto

import android.content.Context
import android.content.Intent
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
import com.allyants.notifyme.NotifyMe
import com.example.proyecto.databinding.FragmentPagosBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class PagosFragment : Fragment() {
    private val userID = FirebaseAuth.getInstance().currentUser!!.uid
    private val db = FirebaseFirestore.getInstance()
    private val collectionRef = db.collection("users")
    private val pagosRef = collectionRef.document(userID).collection("pagos")
    private var mContext: Context? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = "PAGOS"

        val binding = DataBindingUtil.inflate<FragmentPagosBinding>(inflater,
            R.layout.fragment_pagos, container, false)

        val listCon = binding.llayoutConceptos
        val listEst = binding.llayoutEstados
        val listFec = binding.llayoutFecha

        pagosRef.addSnapshotListener { value, e ->
            if (e != null) {
                return@addSnapshotListener
            }

            val pagoCon = ArrayList<String>()
            val pagoEst = ArrayList<String>()
            val pagoFec = ArrayList<String>()
            for (doc in value!!) {
                doc.getString("concepto")?.let {
                    pagoCon.add(it)
                }
                doc.getString("estado")?.let {
                    pagoEst.add(it)
                }
                doc.getString("fecha")?.let {
                    pagoFec.add(it)
                }
            }

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)

            params.setMargins(0, 16, 0, 16)
            params.gravity = Gravity.CENTER

            if(listCon.childCount > 0 ){
                listCon.removeAllViews()
                listEst.removeAllViews()
                listFec?.removeAllViews()
            }

            for (i in 0 until pagoCon.size) {
                val tvCon = TextView(mContext)
                val tvEst = TextView(mContext)
                val tvFec = TextView(mContext)

                tvCon.text = pagoCon[i]
                tvCon.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
                tvCon.setTextColor(mContext!!.getResources().getColor(R.color.colorTextBlack))
                tvCon.layoutParams = params
                tvCon.setOnClickListener {
                    val amountTv = tvCon
                    val amount = amountTv.text.toString()
                    val action =
                        PagosFragmentDirections.actionPagosFragmentToVerPagoFragment(amount)
                    it.findNavController().navigate(action)
                }
                listCon.addView(tvCon)

                tvEst.text = pagoEst[i]
                tvEst.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
                if (tvEst.text.equals("PENDIENTE")) {
                    tvEst.setTextColor(mContext!!.getResources().getColor(R.color.colorRed))
                } else if (tvEst.text.equals("CANCELADO")) {
                    tvEst.setTextColor(mContext!!.getResources().getColor(R.color.colorGreen))
                }
                tvEst.layoutParams = params
                listEst?.addView(tvEst)

                tvFec.text = pagoFec[i]
                tvFec.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
                tvFec.setTextColor(mContext!!.getResources().getColor(R.color.colorTextBlack))
                tvFec.layoutParams = params
                listFec?.addView(tvFec)
            }
        }

        binding.fabAddPago.setOnClickListener {
            it.findNavController()
                .navigate(R.id.action_pagosFragment_to_nuevoPagoFragment)
        }

        return binding.root
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cal = Calendar.getInstance()
        var year = 0
        var month = 0
        var day = 0
        val notificationIntent = Intent(mContext, PagosFragment::class.java)

        pagosRef.whereEqualTo("estado", "PENDIENTE")
            .addSnapshotListener { value, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                val pagoCon = java.util.ArrayList<String>()
                val pagoFec = java.util.ArrayList<String>()
                for (doc in value!!) {
                    doc.getString("concepto")?.let {
                        pagoCon.add(it)
                    }
                    doc.getString("fecha")?.let {
                        pagoFec.add(it)
                    }
                }

                for (x in 0 until pagoCon.size) {
                    val separated = pagoFec[x].split("/")

                    cal.get(Calendar.YEAR)
                    cal.get(Calendar.MONTH)
                    cal.get(Calendar.DAY_OF_MONTH)

                    for (i in 5 downTo 1 step 2) {
                        year = separated[2].toInt()
                        month = separated[1].toInt() - 1
                        day = separated[0].toInt() - i
                        cal.set(Calendar.YEAR, year)
                        cal.set(Calendar.MONTH, month)
                        cal.set(Calendar.DAY_OF_MONTH, day)

                        cal.set(Calendar.HOUR_OF_DAY, 10)
                        cal.set(Calendar.MINUTE, 0)
                        cal.set(Calendar.SECOND, 0)

                        NotifyMe.Builder(mContext)
                            .title("Pago pendiente")
                            .content("Tienes ${i} dias antes del limite de pago de ${pagoCon[x]}")
                            .time(cal)
                            .key("${pagoCon[x]}")
                            .addAction(notificationIntent, "Dismiss", true, false)
                            .addAction(notificationIntent, "Done")
                            .large_icon(R.mipmap.ic_launcher_round)
                            .build()
                    }
                }
                pagosRef.whereEqualTo("estado", "CANCELADO")
                    .addSnapshotListener { value, e ->
                        if (e != null) {
                            return@addSnapshotListener
                        }

                        for (x in 0 until pagoCon.size) {
                            NotifyMe.cancel(mContext,"${pagoCon[x]}")
                        }
                    }
            }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
}
