package ru.d_novikov.bluetoothapp.screens.pulse

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import lecho.lib.hellocharts.gesture.ContainerScrollType
import lecho.lib.hellocharts.gesture.ZoomType
import lecho.lib.hellocharts.model.*
import lecho.lib.hellocharts.util.ChartUtils
import lecho.lib.hellocharts.view.LineChartView
import ru.d_novikov.bluetoothapp.R

class PulseFragment : Fragment() {

    lateinit var chart: LineChartView

    companion object {
        fun getInstance(): PulseFragment {
            return PulseFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_pulse, container, false)
        chart = view.findViewById(R.id.chart)

        setDataToChart()
        return view
    }

    private fun setDataToChart() {
        chart.isInteractive = true
        chart.zoomType = ZoomType.HORIZONTAL_AND_VERTICAL
        chart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL)
        resetViewport(16F)
    }

    private fun resetViewport(rightViewPort: Float) {
        // Reset viewport height range to (-50,50)
        val v = Viewport(chart.maximumViewport)
        v.bottom = -50F
        v.top = 50F
        v.left = 0F
        v.right = rightViewPort
        chart.maximumViewport = v
        chart.currentViewport = v
        setData()
    }

    private fun setData() {
        val values = getValues()
        val line = Line(values)
        line.color = ChartUtils.COLOR_RED
        line.shape = ValueShape.CIRCLE
        line.isCubic = false
        line.isFilled = false
        line.setHasLabels(false)
        line.setHasLines(true)
        line.setHasPoints(false)

        val lines = mutableListOf<Line>()
        lines.add(getCustomLine(35F, ChartUtils.DEFAULT_COLOR))
        lines.add(getCustomLine(-35F, ChartUtils.DEFAULT_COLOR))
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

    private fun getValues(): MutableList<PointValue> {
        val list = mutableListOf<PointValue>()
        list.add(PointValue(0F, 0F))
        list.add(PointValue(1F, 0F))
        list.add(PointValue(1.2F, 5F))
        list.add(PointValue(1.4F, 0F))
        list.add(PointValue(2F, 15F))
        list.add(PointValue(2.5F, -15F))
        list.add(PointValue(2.8F, -5F))
        list.add(PointValue(3.5F, 8F))
        list.add(PointValue(4.5F, 0F))
        list.add(PointValue(6F, 0F))
        list.add(PointValue(6.2F, 4F))
        list.add(PointValue(6.4F, 0F))
        list.add(PointValue(7F, 20F))
        list.add(PointValue(7.2F, -12F))
        list.add(PointValue(7.4F, -0F))
        list.add(PointValue(8F, 8F))
        list.add(PointValue(8.2F, 0F))
        list.add(PointValue(8.5F, 0F))
        list.add(PointValue(9F, 7F))
        list.add(PointValue(9.2F, 0F))
        list.add(PointValue(9.4F, 18F))
        list.add(PointValue(9.6F, -15F))
        list.add(PointValue(9.8F, -0F))
        list.add(PointValue(10.5F, 8F))
        list.add(PointValue(10.8F, 0F))
        list.add(PointValue(11.5F, 0F))
        list.add(PointValue(11.7F, 4F))
        list.add(PointValue(12.0F, 0F))
        list.add(PointValue(12.2F, 20F))
        list.add(PointValue(12.5F, -22F))
        list.add(PointValue(12.7F, 0F))
        list.add(PointValue(13.4F, 8F))
        list.add(PointValue(13.6F, 0F))
        list.add(PointValue(15F, 0F))
        return list
    }
}