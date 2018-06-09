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
        const val UPDATE_STEP = 2

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
            if (calendar - time > 2000) { // обнвляем если прошло 2 секунды с предыдущего обновления
                getValues(((calendar - time) / 2000).toInt())
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
        lines.add(getCustomLine(45F, ChartUtils.DEFAULT_COLOR)) // нижняя граница графика
        lines.add(getCustomLine(150F, ChartUtils.DEFAULT_COLOR)) // верхняя граница графика
        lines.add(line)
        val data = LineChartData(lines)

        val axisX = Axis().setHasSeparationLine(true)
        val axisY = Axis().setHasLines(true)
        axisX.name = "Время(c)"
        axisY.name = "Частота пульса"
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
        val size = if (dataList.isEmpty()) 0F else dataList[dataList.size - 1].x
        list.add(PointValue(0F, value))
        list.add(PointValue(size, value))
        return list
    }

    private fun getValues(seconds: Int) {
        var position = if (dataList.isEmpty()) 0F else dataList[dataList.size - 1].x +1
        var pos = 80
        for (i in 0..seconds) {
            if (position == 0F) {
                dataList.add(PointValue(position, 87f)) // первая точка на графике
                position += UPDATE_STEP
                continue
            }
            if (position.toInt() %10 == 0) {
                pos += 5
            }
            if (position.toInt() %25 == 0) {
                pos -= 10
            }
            val random = Random().nextInt(5) + pos // 80- нижняя граница
            dataList.add(PointValue(position, random.toFloat()))
            position += UPDATE_STEP // шаг раз в 6 секунд
        }
    }
}