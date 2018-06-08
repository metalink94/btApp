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
    private val day = calendar.get(Calendar.DAY_OF_YEAR)
    private val year = calendar.get(Calendar.YEAR)

    private val days = arrayOf("Вс", "Пн", "Вт", "Ср", "Чт", "Пт", "Сб")

    private val STATE_DAY = 0
    private val STATE_WEEK = 1
    private val STATE_MONTH = 2

    /**
     * STATE = 0 - день
     * STATE = 1 - неделя
     * STATE = 2 - месяц
     */
    private var state = STATE_DAY


    fun onCreate() {
        getView()?.setupChart()
        getView()?.setupRefreshLayout()
        setSelection()
        prepareDataToChart()
    }

    private fun setSelection() {
        when (state) {
            STATE_DAY -> getView()?.selectDay()
            STATE_WEEK -> getView()?.selectWeek()
            STATE_MONTH -> getView()?.selectMonth()
            else -> getView()?.selectDay()
        }
    }


    fun onRefresh() {
        prepareDataToChart()
    }

    private fun prepareDataToChart() {
        val result = realm.where<BdModel>().findAll()
        if (result.isEmpty()) return
        getView()?.resetViewport((result.size - 1).toFloat())

        val values = addData(result)
        val axisValues = addAxisValues(values.size)

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

        val axisX = if (axisValues.isEmpty()) Axis() else Axis(axisValues)
        val axisY = Axis().setHasLines(true)
        axisX.name = "Период"
        axisY.name = "Значение"
        data.axisXBottom = axisX
        data.axisYLeft = axisY

        data.baseValue = Float.NEGATIVE_INFINITY
        getView()?.setData(data)
        getView()?.hideRefresh()
    }

    private fun addAxisValues(size: Int): MutableList<AxisValue> {
        return when (state) {
            STATE_DAY, STATE_MONTH -> mutableListOf()
            STATE_WEEK -> weekAxisValue(size)
            else -> mutableListOf()
        }
    }

    private fun weekAxisValue(size: Int): MutableList<AxisValue> {
        val list = mutableListOf<AxisValue>()
        for (i in 0..(size - 1)) {
            list.add(AxisValue(i.toFloat()).setLabel(days[i]))
        }
        return list
    }

    private fun addData(result: RealmResults<BdModel>): MutableList<PointValue> {
        return when (state) {
            STATE_DAY -> dayValues(result) //fakeDayValues()
            STATE_WEEK -> weekValues(result) //fakeWeekValues()
            STATE_MONTH -> monthValues(result) //fakeMonthValues()
            else -> dayValues(result)
        }
    }

    private fun fakeDayValues(): MutableList<PointValue> {
        val list = mutableListOf<PointValue>()
        list.add(PointValue(0F, 0F))
        list.add(PointValue(6F, 0F))
        list.add(PointValue(7F, 54F))
        list.add(PointValue(12F, 72F))
        list.add(PointValue(13F, 116F))
        list.add(PointValue(14F, 90F))
        list.add(PointValue(18F, 70F))
        list.add(PointValue(20F, 90F))
        list.add(PointValue(22F, 62F))
        list.add(PointValue(23F, 46F))
        return list
    }

    private fun fakeWeekValues(): MutableList<PointValue> {
        val list = mutableListOf<PointValue>()
        list.add(PointValue(0F, 67F))
        list.add(PointValue(1F, 86F))
        list.add(PointValue(2F, 54F))
        list.add(PointValue(3F, 0F))
        list.add(PointValue(4F, 116F))
        list.add(PointValue(5F, 40F))
        list.add(PointValue(6F, 0F))
        return list
    }

    private fun fakeMonthValues(): MutableList<PointValue> {
        val list = mutableListOf<PointValue>()
        list.add(PointValue(0F, 67F))
        list.add(PointValue(1F, 86F))
        list.add(PointValue(2F, 54F))
        list.add(PointValue(3F, 0F))
        list.add(PointValue(4F, 116F))
        list.add(PointValue(5F, 40F))
        list.add(PointValue(6F, 0F))
        list.add(PointValue(7F, 67F))
        list.add(PointValue(8F, 72F))
        list.add(PointValue(9F, 56F))
        list.add(PointValue(10F, 100F))
        list.add(PointValue(11F, 0F))
        list.add(PointValue(30F, 0F))
        return list
    }

    private fun getCalendarForNow(): Calendar {
        val calendar = GregorianCalendar.getInstance()
        calendar.time = Date()
        return calendar
    }

    private fun setTimeToBeginningOfDay(calendar: Calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
    }

    private fun setTimeToEndofDay(calendar: Calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
    }

    fun getDateRange(): Pair<Date, Date> {
        var begining: Date
        var end: Date

        val calendar1 = getCalendarForNow()
        calendar1.set(Calendar.DAY_OF_MONTH,
                calendar1.getActualMinimum(Calendar.DAY_OF_MONTH))
        setTimeToBeginningOfDay(calendar1)
        begining = calendar1.time

        val calendar2 = getCalendarForNow()
        calendar2.set(Calendar.DAY_OF_MONTH,
                calendar2.getActualMaximum(Calendar.DAY_OF_MONTH))
        setTimeToEndofDay(calendar2)
        end = calendar2.time

        return Pair(begining, end)
    }

    private fun monthValues(result: RealmResults<BdModel>): MutableList<PointValue> {
        val list = mutableListOf<PointValue>()
        val monthList = mutableListOf<BdModel>()
        val pair = getDateRange()
        val cal = Calendar.getInstance()

        for (bdModel in result) {
            if (pair.first.before(bdModel.valueX)) {
                monthList.add(bdModel)
            }
        }

        for (i in 0..cal.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            var resultData = 0
            var size = 0
            for (bdModel in monthList) {
                val monthCalendar = Calendar.getInstance()
                monthCalendar.time = bdModel.valueX
                cal.time = pair.first
                cal.add(Calendar.DATE, i)
                if (cal.get(Calendar.DAY_OF_YEAR) == monthCalendar.get(Calendar.DAY_OF_YEAR) &&
                        cal.get(Calendar.YEAR) == monthCalendar.get(Calendar.YEAR)) {
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
        val date = getLastWeek()

        for (bdModel in result) {
            if (date.before(bdModel.valueX)) {
                weekList.add(bdModel)
            }
        }

        for (i in 0..6) {
            var resultData = 0
            var size = 0
            for (bdModel in weekList) {
                val weekCalendar = Calendar.getInstance()
                weekCalendar.time = bdModel.valueX
                cal.time = date
                cal.add(Calendar.DATE, i)
                if (cal.get(Calendar.DAY_OF_YEAR) == weekCalendar.get(Calendar.DAY_OF_YEAR) &&
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
            if (day == calendarModel.get(Calendar.DAY_OF_YEAR) &&
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

    fun onDayClick() {
        if (state == STATE_DAY) return

        state = STATE_DAY
        setSelection()
        prepareDataToChart()
    }

    fun onWeekClick() {
        if (state == STATE_WEEK) return

        state = STATE_WEEK
        setSelection()
        prepareDataToChart()
    }

    fun onMonthClick() {
        if (state == STATE_MONTH) return

        state = STATE_MONTH
        setSelection()
        prepareDataToChart()
    }
}