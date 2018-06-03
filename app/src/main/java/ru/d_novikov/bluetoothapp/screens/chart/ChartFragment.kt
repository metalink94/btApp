package ru.d_novikov.bluetoothapp.screens.chart

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import lecho.lib.hellocharts.gesture.ContainerScrollType
import lecho.lib.hellocharts.gesture.ZoomType
import lecho.lib.hellocharts.model.LineChartData
import lecho.lib.hellocharts.model.Viewport
import lecho.lib.hellocharts.view.LineChartView
import ru.d_novikov.bluetoothapp.R


class ChartFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener, ChartView {

    lateinit var chart: LineChartView

    lateinit var swipeToRefresh: SwipeRefreshLayout

    lateinit var day: TextView

    lateinit var week: TextView

    lateinit var month: TextView

    val chartPresenter = ChartPresenter()

    companion object {

        fun getInstance(): ChartFragment {
            return ChartFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_chart_screen, container, false)
        init(view)
        setOnClick()
        chartPresenter.setView(this)
        chartPresenter.onCreate()
        return view
    }

    private fun init(view: View) {
        chart = view.findViewById(R.id.chart)
        swipeToRefresh = view.findViewById(R.id.swipe)
        day = view.findViewById(R.id.day)
        week = view.findViewById(R.id.week)
        month = view.findViewById(R.id.month)
    }

    private fun setOnClick() {
        day.setOnClickListener { chartPresenter.onDayClick() }
        week.setOnClickListener { chartPresenter.onWeekClick() }
        month.setOnClickListener { chartPresenter.onMonthClick() }
    }

    override fun setupChart() {
        chart.isInteractive = true
        chart.zoomType = ZoomType.HORIZONTAL_AND_VERTICAL
        chart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL)
        resetViewport(10F)
    }

    override fun setupRefreshLayout() {
        swipeToRefresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light)
    }

    override fun resetViewport(rightViewPort: Float) {
        // Reset viewport height range to (0,300)
        val v = Viewport(chart.maximumViewport)
        v.bottom = 0F
        v.top = 300F
        v.left = 0F
        v.right = rightViewPort
        chart.maximumViewport = v
        chart.currentViewport = v
    }

    fun updateChart() {
        chartPresenter.onUpdateChart()
    }

    override fun setData(data: LineChartData) {
        chart.lineChartData = data
    }


    override fun onRefresh() {
        chartPresenter.onRefresh()
    }

    override fun hideRefresh() {
        swipeToRefresh.isRefreshing = false
    }

    override fun selectDay() {
        day.background = ContextCompat.getDrawable(requireContext(), R.drawable.day_select)
        week.background = ContextCompat.getDrawable(requireContext(), R.drawable.week_backround)
        month.background = ContextCompat.getDrawable(requireContext(), R.drawable.month_background)
    }

    override fun selectWeek() {
        day.background = ContextCompat.getDrawable(requireContext(), R.drawable.day_background)
        week.background = ContextCompat.getDrawable(requireContext(), R.drawable.week_select)
        month.background = ContextCompat.getDrawable(requireContext(), R.drawable.month_background)
    }

    override fun selectMonth() {
        day.background = ContextCompat.getDrawable(requireContext(), R.drawable.day_background)
        week.background = ContextCompat.getDrawable(requireContext(), R.drawable.week_backround)
        month.background = ContextCompat.getDrawable(requireContext(), R.drawable.month_select)
    }
}