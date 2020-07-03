package com.example.proyecto

import android.app.Activity
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.proyecto.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_perfil.*
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.round

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {
    private val userID = FirebaseAuth.getInstance().currentUser!!.uid
    private val db = FirebaseFirestore.getInstance()
    private val collectionRef = db.collection("users")
    private val ingresosRef = collectionRef.document(userID).collection("ingresos")
    private val gastosRef = collectionRef.document(userID).collection("gastos")
    private val pagosRef = collectionRef.document(userID).collection("pagos")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Mostrando ActionBar
        (activity as AppCompatActivity).supportActionBar?.show()
        //Setting Title
        (activity as AppCompatActivity).supportActionBar?.title = "NexusSave"

        val binding = DataBindingUtil.inflate<FragmentHomeBinding>(inflater,
            R.layout.fragment_home, container, false)

        centerTitle()
        setHasOptionsMenu(true)
        var total:Double
        var ingresos: Double
        var gastos:Double
        var pagos:Double

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
            //ingresos = round(ingresos*100) /100
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
                //gastos = round(gastos*100) /100
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
                    //pagos = round(pagos * 100) /100
                    Log.d(ContentValues.TAG, pagos.toString())
                    total = ingresos - gastos - pagos
                    binding.tvDinero.text = "%.2f".format(total)
                }
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.home_perfil, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.perfilFragment -> view!!.findNavController()
                .navigate(R.id.action_homeFragment_to_perfilFragment)

        }
        return super.onOptionsItemSelected(item)
    }
}
