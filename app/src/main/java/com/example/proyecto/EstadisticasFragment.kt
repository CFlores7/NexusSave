package com.example.proyecto

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.proyecto.databinding.FragmentEstadisticasBinding
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


/**
 * A simple [Fragment] subclass.
 */
class EstadisticasFragment : Fragment() {
    //private var yData = arrayOf(25F, 50F, 19F, 6F)
    //private var xData = arrayOf("Luz","Ingresos","Internet","Agua")
    private val userID = FirebaseAuth.getInstance().currentUser!!.uid
    private val db = FirebaseFirestore.getInstance()
    private val collectionRef = db.collection("users")
    private val ingresosRef = collectionRef.document(userID).collection("ingresos")
    private val gastosRef = collectionRef.document(userID).collection("gastos")
    private val pagosRef = collectionRef.document(userID).collection("pagos")
    private lateinit var pieChart: PieChart

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        (activity as AppCompatActivity).supportActionBar?.title = ""
        val binding = DataBindingUtil.inflate<FragmentEstadisticasBinding>(inflater,
            R.layout.fragment_estadisticas, container, false)

        pieChart = binding.PieChart

        pieChart.description.text = "Manejo de su dinero"
        pieChart.isRotationEnabled = true
        pieChart.holeRadius = 15F
        pieChart.setTransparentCircleAlpha(0)
        pieChart.setCenterTextSize(10F)

        addDataSet()

        // Inflate the layout for this fragment
        return binding.root
    }
    private fun addDataSet() {
        val yEntrys = arrayListOf<PieEntry>()
        val xEntrys = arrayListOf<String>()
        val yData = ArrayList<Float>()
        val xData = ArrayList<String>()

        ingresosRef.addSnapshotListener { value, e ->
            if (e != null) {
                return@addSnapshotListener
            }

            for (doc in value!!) {
                doc.getString("concepto")?.let {
                    xData.add(it)
                }
                doc.getString("monto")?.let {
                    yData.add(it.toFloat())
                }
            }
            gastosRef.addSnapshotListener { value, e ->
                if (e != null) {
                    return@addSnapshotListener
                }

                for (doc in value!!) {
                    doc.getString("concepto")?.let {
                        xData.add(it)
                    }
                    doc.getString("monto")?.let {
                        yData.add(it.toFloat())
                    }
                }
                pagosRef.addSnapshotListener { value, e ->
                    if (e != null) {
                        return@addSnapshotListener
                    }

                    for (doc in value!!) {
                        if(doc.getString("estado").equals("CANCELADO")) {
                            doc.getString("concepto")?.let {
                                xData.add(it)
                            }
                            doc.getString("monto")?.let {
                                yData.add(it.toFloat())
                            }
                        }
                        for (i in yData.indices) {
                            yEntrys.add(PieEntry(yData[i],i))
                        }
                        for (i in xData.indices) {
                            xEntrys.add(xData[i])
                        }
                        //Create The Data Set
                        var pieDataSet = PieDataSet(yEntrys, "Estadisticas")
                        pieDataSet.sliceSpace = 2F
                        pieDataSet.valueTextSize = 12F

                        //Add color to dataset
                        val colors = ArrayList<Int>()
                        colors.add(Color.BLUE)
                        colors.add(Color.CYAN)
                        colors.add(Color.LTGRAY)
                        colors.add(Color.RED)

                        pieDataSet.colors = colors

                        //Add legend to chart
                        var legend = pieChart.legend
                        legend.form = Legend.LegendForm.CIRCLE

                        //Create Pie Data Object
                        val pieData = PieData(pieDataSet)
                        pieChart.data = pieData
                        pieChart.invalidate()
                    }
                }
            }
        }


    }

}
