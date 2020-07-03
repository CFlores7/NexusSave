package com.example.proyecto

import android.content.ContentValues.TAG
import android.os.Bundle
import android.print.PrintAttributes
import android.provider.ContactsContract.CommonDataKinds.Note
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginBottom
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.proyecto.databinding.FragmentIngresosBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.*
import org.w3c.dom.Text


/**
 * A simple [Fragment] subclass.
 */
class IngresosFragment : Fragment() {
    private val userID = FirebaseAuth.getInstance().currentUser!!.uid
    private val db = FirebaseFirestore.getInstance()
    private val collectionRef = db.collection("users")
    private val ingresosRef = collectionRef.document(userID).collection("ingresos")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = "INGRESOS"

        val binding = DataBindingUtil.inflate<FragmentIngresosBinding>(inflater,
            R.layout.fragment_ingresos, container, false)

        val list = binding.linearIngresos
        
        ingresosRef.addSnapshotListener { value, e ->
            if (e != null) {
                return@addSnapshotListener
            }

            val ingresos = ArrayList<String>()
            for (doc in value!!) {
                doc.getString("concepto")?.let {
                    ingresos.add(it)
                }
            }

            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)
            params.setMargins(0, 16, 0, 16)
            params.gravity = Gravity.CENTER

            for (i in 0 until ingresos.size) {
                val tv = TextView(context)
                tv.text = ingresos[i]
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24F)
                tv.setTextColor(getResources().getColor(R.color.colorTextBlack))
                tv.layoutParams = params
                //binding.tvIngresos?.append(ingresos[i])
                list?.addView(tv)
            }

        }

        binding.fabAddIngreso.setOnClickListener{
            it.findNavController()
                .navigate(R.id.action_ingresosFragment_to_nuevoIngresoFragment)
        }

        return binding.root
    }
}
