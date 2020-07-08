package com.example.proyecto

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.proyecto.databinding.FragmentVerPagoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_ver_pago.view.*
import java.util.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class VerPagoFragment : Fragment() {
    private val userID = FirebaseAuth.getInstance().currentUser!!.uid
    private val db = FirebaseFirestore.getInstance()
    private val collectionRef = db.collection("users")
    private val pagosRef = collectionRef.document(userID).collection("pagos")
    private var mContext: Context? = null
    val args: VerPagoFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = "DETALLE PAGO"

        val binding = DataBindingUtil.inflate<FragmentVerPagoBinding>(inflater,
            R.layout.fragment_ver_pago, container, false)
        val amount = args.concepto
        var docID: String = ""

        centerTitle()

        binding.btBorrar.setOnClickListener {
            pagosRef.whereEqualTo("concepto", amount).get()
                .addOnSuccessListener { documents ->
                    for (document in documents){
                        docID = document.id
                    }
                    pagosRef.document(docID).delete()
                        .addOnSuccessListener {
                            Toast.makeText(mContext,"¡Pago eliminado con exito!", Toast.LENGTH_LONG).show()
                        }
                }
            activity!!.onBackPressed()
        }

        binding.btRegresar.setOnClickListener {
            pagosRef.whereEqualTo("concepto", amount).get()
                .addOnSuccessListener { documents ->
                    for (document in documents){
                        docID = document.id
                    }
                    val pago = HashMap<String, Any>()
                    if (binding.rbCancelado.isChecked){
                        pago["estado"] = "CANCELADO"
                    } else {
                        pago["estado"] = "PENDIENTE"
                    }
                    pagosRef.document(docID).update(pago)
                }

            activity!!.onBackPressed()
        }

        return binding.root
    }
    //Setting Title
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tvCon: TextView = view.findViewById(R.id.tvConceptoIngreso)
        val tvCan: TextView = view.findViewById(R.id.tvCantidadIngreso)
        val rbCan: RadioButton = view.findViewById(R.id.rbCancelado)
        val rbPen: RadioButton = view.findViewById(R.id.rbPendiente)
        val tvFec: TextView = view.findViewById(R.id.tvFechaIngreso)
        val amount = args.concepto
        tvCon.text = amount

        pagosRef.whereEqualTo("concepto", amount)
            .addSnapshotListener { value, e ->
                if (e != null) {
                    return@addSnapshotListener
                }

                var cantidad = ""
                var estado: Boolean = false
                var fecha = ""
                for (doc in value!!) {
                    doc.getString("monto")?.let {
                        cantidad = it
                    }
                    doc.getString("estado")?.let {
                        if(it.equals("CANCELADO")){
                            estado = true
                        }
                    }
                    doc.getString("fecha")?.let {
                        fecha = it
                    }
                }
                tvCan.text = "$" + cantidad
                tvFec.text = fecha
                if(estado.equals(true)){
                    rbCan.isChecked = true
                }else{
                    rbPen.isChecked = true
                }
        //(activity as AppCompatActivity).supportActionBar?.title = "PAGO"
    }}
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
