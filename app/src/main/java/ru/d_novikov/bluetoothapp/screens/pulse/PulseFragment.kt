package ru.d_novikov.bluetoothapp.screens.pulse

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import lecho.lib.hellocharts.gesture.ContainerScrollType
import lecho.lib.hellocharts.gesture.ZoomType
import lecho.lib.hellocharts.model.*
import lecho.lib.hellocharts.util.ChartUtils
import lecho.lib.hellocharts.view.LineChartView
import ru.d_novikov.bluetoothapp.R
import java.util.*

class PulseFragment : Fragment() {

    lateinit var swipe: SwipeRefreshLayout
    lateinit var chart: LineChartView
    private val dataList = mutableListOf<PointValue>()
    private var time = Calendar.getInstance().timeInMillis

    companion object {
        fun getInstance(): PulseFragment {
            return PulseFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_pulse, container, false)
        chart = view.findViewById(R.id.chart)
        swipe = view.findViewById(R.id.swipe)
        swipe.setOnRefreshListener {
            swipe.isRefreshing = false
            val calendar = Calendar.getInstance().timeInMillis
            if (calendar - time > 1000) {
                getValues(((calendar - time)/ 1000).toInt())
                setDataToChart()
            }
            time = calendar
        }
        getValues(10)
        setDataToChart()
        return view
    }

    private fun setDataToChart() {
        chart.isInteractive = true
        chart.zoomType = ZoomType.HORIZONTAL_AND_VERTICAL
        chart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL)
        resetViewport(dataList.size.toFloat())
    }

    private fun resetViewport(rightViewPort: Float) {
        // Reset viewport height range to (-50,50)
        val v = Viewport(chart.maximumViewport)
        v.bottom = 0F
        v.top = 300F
        v.left = 0F
        v.right = rightViewPort
        chart.maximumViewport = v
        chart.currentViewport = v
        setData()
    }

    private fun setData() {
        val line = Line(dataList)
        line.color = ChartUtils.COLOR_RED
        line.shape = ValueShape.CIRCLE
        line.isCubic = false
        line.isFilled = false
        line.setHasLabels(false)
        line.setHasLines(true)
        line.setHasPoints(false)

        val lines = mutableListOf<Line>()
        lines.add(getCustomLine(0F, ChartUtils.DEFAULT_COLOR))
        lines.add(getCustomLine(300F, ChartUtils.DEFAULT_COLOR))
        lines.add(getCustomLine(0F, ChartUtils.DEFAULT_DARKEN_COLOR))
        lines.add(line)
        val data = LineChartData(lines)

        val axisX = Axis().setHasSeparationLine(true)
        val axisY = Axis().setHasLines(true)
        axisX.name = "Время"
        axisY.values = mutableListOf()
        axisY.name = "Значение"
        data.axisXBottom = axisX
        data.axisYLeft = axisY

        data.baseValue = 0F
        chart.lineChartData = data
    }

    private fun getCustomLine(value: Float, color: Int): Line {
        val line = Line(getZeroValues(value))
        line.color = color
        line.shape = ValueShape.CIRCLE
        line.isCubic = false
        line.isFilled = false
        line.setHasLabels(false)
        line.setHasLines(true)
        line.setHasPoints(false)
        return line
    }

    private fun getZeroValues(value: Float): MutableList<PointValue> {
        val list = mutableListOf<PointValue>()
        list.add(PointValue(0F, value))
        list.add(PointValue(15F, value))
        return list
    }

    private fun getValues(seconds: Int) {
        val position = if (dataList.isEmpty()) 0F else (dataList.size - 1).toFloat()
        for (i in 0..seconds) {
            dataList.add(PointValue(position + 1F, Random().nextInt(60-10).toFloat()))
        }
    }
}