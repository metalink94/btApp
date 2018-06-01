package ru.d_novikov.bluetoothapp.screens.chart

import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where
import lecho.lib.hellocharts.model.*
import lecho.lib.hellocharts.util.ChartUtils
import ru.d_novikov.bluetoothapp.models.BdModel
import ru.d_novikov.bluetoothapp.mvp.ViewPresenter
import java.util.*



class ChartPresenter : ViewPresenter<ChartView>() {

    val realm = Realm.getDefaultInstance()

    //init calendar
    private val calendar = Calendar.getInstance()
    private val day = calendar.get(Calendar.DAY_OF_MONTH)
    private val month = calendar.get(Calendar.MONTH)
    private val year = calendar.get(Calendar.YEAR)

    /**
     * STATE = 0 - день
     * STATE = 1 - неделя
     * STATE = 2 - месяц
     */
    var state = 1

    fun onCreate() {
        getView()?.setupChart()
        getView()?.setupRefreshLayout()
        prepareDataToChart()
    }


    fun onRefresh() {
        prepareDataToChart()
    }

    private fun prepareDataToChart() {
        val result = realm.where<BdModel>().findAll()
        if (result.isEmpty()) return
        getView()?.resetViewport((result.size - 1).toFloat())

        val values = addData(result)

        val line = Line(values)
        line.color = ChartUtils.COLOR_BLUE
        line.shape = ValueShape.CIRCLE
        line.isCubic = false
        line.isFilled = true
        line.setHasLabels(false)
        line.setHasLines(true)
        line.setHasPoints(true)

        val lines = mutableListOf<Line>()
        lines.add(line)
        val data = LineChartData(lines)

        val axisX = Axis()
        val axisY = Axis().setHasLines(true)
        axisX.name = "Axis X"
        axisY.name = "Axis Y"
        data.axisXBottom = axisX
        data.axisYLeft = axisY

        data.baseValue = Float.NEGATIVE_INFINITY
        getView()?.setData(data)
    }

    private fun addData(result: RealmResults<BdModel>): MutableList<PointValue> {
        return when (state) {
            0 -> dayValues(result)
            1 -> weekValues(result)
            2 -> monthValues(result)
            else -> dayValues(result)
        }
    }

    private fun monthValues(result: RealmResults<BdModel>): MutableList<PointValue> {
        return mutableListOf()
    }

    private fun getLastWeek(): Date {
        val dayBeforeThisWeek = GregorianCalendar()
        val dayFromMonday = (dayBeforeThisWeek.get(Calendar.DAY_OF_WEEK) + 7 - Calendar.MONDAY) % 7
        dayBeforeThisWeek.add(Calendar.DATE, -dayFromMonday - 1)
        return dayBeforeThisWeek.time
    }

    private fun weekValues(result: RealmResults<BdModel>): MutableList<PointValue> {
        val list = mutableListOf<PointValue>()
        val weekList = mutableListOf<BdModel>()
        val cal = Calendar.getInstance()
        cal.time = getLastWeek()

        for (bdModel in result) {
            if (cal.time.before(bdModel.valueX)) {
                weekList.add(bdModel)
            }
        }

        for (i in 0..6) {
            var resultData = 0
            var size = 0
            for (bdModel in weekList) {
                val weekCalendar = Calendar.getInstance()
                weekCalendar.time = bdModel.valueX
                cal.add(Calendar.DATE, -6 + i)
                if (cal.get(Calendar.DAY_OF_MONTH) == weekCalendar.get(Calendar.DAY_OF_MONTH) &&
                        cal.get(Calendar.MONTH) == weekCalendar.get(Calendar.MONTH) &&
                        cal.get(Calendar.YEAR) == weekCalendar.get(Calendar.YEAR)) {
                    resultData += bdModel.valueY
                    size += 1
                }
            }
            if (size == 0) {
                list.add(PointValue(i.toFloat(), 0F))
            } else {
                val endResult: Float = (resultData / size).toFloat()
                list.add(PointValue(i.toFloat(), endResult))
            }
        }
        return list
    }

    private fun dayValues(result: RealmResults<BdModel>): MutableList<PointValue> {
        val list = mutableListOf<PointValue>()
        val todayList = mutableListOf<BdModel>()

        //find only today points
        for (bdModel in result) {
            val calendarModel = Calendar.getInstance()
            calendarModel.time = bdModel.valueX
            if (day == calendarModel.get(Calendar.DAY_OF_MONTH) &&
                    month == calendar.get(Calendar.MONTH) &&
                    year == calendar.get(Calendar.YEAR)) {
                todayList.add(bdModel)
            }
        }

        // check time
        for (i in 0..23) {
            val calendarModel = Calendar.getInstance()
            var resultData = 0
            var size = 0
            for (bdModel in todayList) {
                calendarModel.time = bdModel.valueX
                if (i == calendarModel.get(Calendar.HOUR_OF_DAY)) {
                    resultData += bdModel.valueY
                    size += 1
                }
            }
            if (size == 0) {
                list.add(PointValue(i.toFloat(), 0F))
            } else {
                val endResult: Float = (resultData / size).toFloat()
                list.add(PointValue(i.toFloat(), endResult))
            }
        }
        return list
    }

    fun onUpdateChart() {
        prepareDataToChart()
    }
}