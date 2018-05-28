package ru.d_novikov.bluetoothapp.screens.chart

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.BarLineChartBase
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.ChartData
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import io.realm.Realm
import io.realm.kotlin.where
import ru.d_novikov.bluetoothapp.R
import ru.d_novikov.bluetoothapp.models.BdModel
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.formatter.IAxisValueFormatter


class ChartFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    lateinit var chart: LineChart
    lateinit var swipeToRefresh: SwipeRefreshLayout
    val realm = Realm.getDefaultInstance()

    companion object {

        fun getInstance(): ChartFragment {
            return ChartFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_chart_screen, container, false)
        chart = view.findViewById(R.id.chart)
        swipeToRefresh = view.findViewById(R.id.swipe)
        setup(chart)
        chart.axisLeft.axisMaximum = 300f
        chart.axisLeft.setValueFormatter({ value, axis -> value.toString() })
        chart.axisLeft.axisMinimum = 0f
        chart.axisLeft.setDrawGridLines(false)
        chart.xAxis.setDrawGridLines(false)

        swipeToRefresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light)
        setData()
        return view
    }

    fun updateChart() {
        Log.d("JavaClass", "Need to Update chart")
        Log.d("JavaClass", "Realm object count ${realm.where<BdModel>().findAll().size}")
        setData()
    }

    private fun setData() {
        val result = realm.where<BdModel>().findAll()
        if (result.isEmpty()) return
        val yVals1 = ArrayList<Entry>()
        for ((i, obj) in result.withIndex()) {
            yVals1.add(Entry(i.toFloat(), obj.valueY.toFloat()))
        }
        val set: LineDataSet
        if (chart.data != null) {
            set = chart.data.getDataSetByIndex(0) as LineDataSet
            set.values = yVals1
            chart.data.notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            set = LineDataSet(yVals1, "Данные")

            set.axisDependency = AxisDependency.LEFT
            set.color = ColorTemplate.getHoloBlue()
            set.setCircleColor(Color.WHITE)
            set.lineWidth = 2f
            set.circleRadius = 3f
            set.fillAlpha = 65
            set.fillColor = ColorTemplate.getHoloBlue()
            set.highLightColor = Color.rgb(244, 117, 117)
            set.setDrawCircleHole(false)
            val data = LineData(set)
            styleData(data)

            // set data
            chart.data = data
        }
    }

    protected fun styleData(data: ChartData<*>) {
        data.setValueTextSize(8f)
        data.setValueTextColor(Color.DKGRAY)
        data.setValueFormatter(PercentFormatter())
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

    override fun onRefresh() {
        updateChart()

    }
}