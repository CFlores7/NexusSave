package com.royallock.nexussave

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.royallock.nexussave.databinding.FragmentEstadisticasBinding
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


/**
 * A simple [Fragment] subclass.
 */
class EstadisticasFragment : Fragment() {
    private val userID = FirebaseAuth.getInstance().currentUser!!.uid
    private val db = FirebaseFirestore.getInstance()
    private val collectionRef = db.collection("users")
    private val ingresosRef = collectionRef.document(userID).collection("ingresos")
    private val gastosRef = collectionRef.document(userID).collection("gastos")
    private val pagosRef = collectionRef.document(userID).collection("pagos")
    private lateinit var pieChart: PieChart
    private var mContext: Context? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        (activity as AppCompatActivity).supportActionBar?.title = ""
        val binding = DataBindingUtil.inflate<FragmentEstadisticasBinding>(inflater,
            R.layout.fragment_estadisticas, container, false)

        pieChart = binding.PieChart

        pieChart.description.text = "Manejo de su dinero"
        pieChart.description.textSize = 16F
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
                    val MY_COLORS = intArrayOf(
                        Color.rgb(0, 126, 214),
                        Color.rgb(131, 153, 235),
                        Color.rgb(142, 108, 239),
                        Color.rgb(156, 70, 208),
                        Color.rgb(199, 88, 208),
                        Color.rgb(224, 30, 132),
                        Color.rgb(255, 0, 0),
                        Color.rgb(255, 115, 0),
                        Color.rgb(255, 175, 0),
                        Color.rgb(255, 236, 0),
                        Color.rgb(213, 243, 11),
                        Color.rgb(82, 215, 38),
                        Color.rgb(27, 170, 47),
                        Color.rgb(45, 203, 117),
                        Color.rgb(38, 215, 174),
                        Color.rgb(124, 221, 221),
                        Color.rgb(95, 183, 212),
                        Color.rgb(151, 217, 255)
                    )
                    val colors = ArrayList<Int>()

                    for (c in MY_COLORS) colors.add(c)

                    pieDataSet.setColors(colors)

                    //Show selected value in Pie Chart
                    pieChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                        override fun onValueSelected(e: Entry?, h: Highlight?) {
                            if (e == null) return

                            Toast.makeText(
                                mContext,
                                "Concepto: "+ xData[h?.x!!.toInt()] + "\n Monto: $" + e.y,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        override fun onNothingSelected() {}
                    })

                    //Add legend to chart
                    var legend = pieChart.legend
                    legend.form = Legend.LegendForm.CIRCLE

                    //Create Pie Data Object
                    val data = PieData(pieDataSet)

                    data.setValueFormatter(PercentFormatter(pieChart))

                    pieChart.data = data
                    pieChart.setUsePercentValues(true)
                    pieChart.highlightValue(null)
                    pieChart.invalidate()
                }
            }
        }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
}
