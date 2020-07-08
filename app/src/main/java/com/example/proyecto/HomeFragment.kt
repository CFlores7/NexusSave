package com.example.proyecto

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
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
import com.allyants.notifyme.NotifyMe
import com.example.proyecto.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_perfil.*
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList
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
    private var mContext: Context? = null

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cal = Calendar.getInstance()
        var year = 0
        var month = 0
        var day = 0
        val notificationIntent = Intent(mContext, HomeFragment::class.java)

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
                            .addAction(notificationIntent, "Descartar", true, false)
                            .addAction(notificationIntent, "Entendido")
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
