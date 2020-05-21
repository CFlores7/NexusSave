package com.example.proyecto

import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.adapters.CalendarViewBindingAdapter.setDate
import com.example.proyecto.databinding.FragmentNuevoGastoBinding
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class NuevoGastoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = "NUEVO GASTO"

        val binding = DataBindingUtil.inflate<FragmentNuevoGastoBinding>(inflater,
            R.layout.fragment_nuevo_gasto, container, false)

        centerTitle()

        Toast.makeText(this.activity,
            "El gasto no puede registrarse si es anterior a 3 dias.",
            Toast.LENGTH_LONG).show()

        binding.btFecha.setOnClickListener {
            chooseDate(binding.etFecha)
        }

        return binding.root
    }
    //Dialogo para elegir fecha
    private fun chooseDate(etFecha: EditText){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val minDay = 3*24*60*60*1000

        val dpd = DatePickerDialog(
            activity as AppCompatActivity, android.R.style.Theme_Material_Dialog_MinWidth,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                // Display Selected date in textbox
                etFecha.setText("" + dayOfMonth + "/" + monthOfYear + "/" + year)
            },
            year,
            month,
            day
        )
        dpd.datePicker.minDate = (System.currentTimeMillis() - minDay)
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
}
