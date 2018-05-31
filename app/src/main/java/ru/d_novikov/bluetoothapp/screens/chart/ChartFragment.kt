package ru.d_novikov.bluetoothapp.screens.chart

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.realm.Realm
import io.realm.kotlin.where
import lecho.lib.hellocharts.gesture.ContainerScrollType
import lecho.lib.hellocharts.gesture.ZoomType
import lecho.lib.hellocharts.view.LineChartView
import ru.d_novikov.bluetoothapp.R
import ru.d_novikov.bluetoothapp.models.BdModel


class ChartFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    lateinit var chart: LineChartView
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
        setupChart()

        swipeToRefresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light)
        setData()
        return view
    }

    private fun setupChart() {
        chart.isInteractive = true
        chart.zoomType = ZoomType.HORIZONTAL_AND_VERTICAL
        chart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL)
    }

    fun updateChart() {
        setData()
    }

    private fun setData() {
        val result = realm.where<BdModel>().findAll()
        if (result.isEmpty()) return

    }


    override fun onRefresh() {
        updateChart()
    }
}