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


/**
 * A simple [Fragment] subclass.
 */
class EstadisticasFragment : Fragment() {
    private var yData = arrayOf(25F, 50F, 19F, 6F)
    private var xData = arrayOf("Luz","Ingresos","Internet","Agua")
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
