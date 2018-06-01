package ru.d_novikov.bluetoothapp.screens.chart

import lecho.lib.hellocharts.model.LineChartData
import ru.d_novikov.bluetoothapp.mvp.IView

interface ChartView : IView {
    fun setupChart()
    fun setupRefreshLayout()
    fun resetViewport(count: Float)
    fun setData(data: LineChartData)
}