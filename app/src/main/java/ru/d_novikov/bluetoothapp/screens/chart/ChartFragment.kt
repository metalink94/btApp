package ru.d_novikov.bluetoothapp.screens.chart

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.BarLineChartBase
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.PercentFormatter
import ru.d_novikov.bluetoothapp.R


class ChartFragment : Fragment() {

    lateinit var chart: LineChart

    companion object {

        fun getInstance(): ChartFragment {
            return ChartFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_chart_screen, container, false)
        chart = view.findViewById(R.id.chart)
        setup(chart)
        chart.axisLeft.setAxisMaximum(150f)
        chart.axisLeft.setAxisMinimum(0f)
        chart.axisLeft.setDrawGridLines(false)
        chart.xAxis.setDrawGridLines(false)
        return view
    }

    fun update(data: String) {
        Log.d("ChartFragment", "data received $data")
    }

    private fun setup(chart: Chart<*>) {

        // no description text
        chart.description.isEnabled = false

        // enable touch gestures
        chart.setTouchEnabled(true)

        if (chart is BarLineChartBase<*>) {

            val mChart = chart

            mChart.setDrawGridBackground(false)

            // enable scaling and dragging
            mChart.isDragEnabled = true
            mChart.setScaleEnabled(true)

            // if disabled, scaling can be done on x- and y-axis separately
            mChart.setPinchZoom(false)

            val leftAxis = mChart.axisLeft
            leftAxis.removeAllLimitLines() // reset all limit lines to avoid overlapping lines
            leftAxis.textSize = 8f
            leftAxis.textColor = Color.DKGRAY
            leftAxis.valueFormatter = PercentFormatter()

            val xAxis = mChart.xAxis
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.textSize = 8f
            xAxis.textColor = Color.DKGRAY

            mChart.axisRight.isEnabled = false
        }
    }
}