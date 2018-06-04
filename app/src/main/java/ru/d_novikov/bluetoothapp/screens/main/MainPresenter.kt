package ru.d_novikov.bluetoothapp.screens.main

import android.bluetooth.BluetoothAdapter
import ru.d_novikov.bluetoothapp.models.SaveModel
import ru.d_novikov.bluetoothapp.mvp.ViewPresenter
import ru.d_novikov.bluetoothapp.utils.AlertBuilder
import java.text.SimpleDateFormat
import java.util.*


class MainPresenter : ViewPresenter<MainView>() {

    companion object {
        const val STATE_STRESS = 0
        const val STATE_SLEEP = 1
        const val STATE_STABLE = 2
    }

    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

    val dataList = mutableListOf<SaveModel>()
    val sdf = SimpleDateFormat("HH:mm:ss.SSS")
    private var isAlertShow = false

    fun onCreate() {
        if (bluetoothAdapter == null) {
            getView()?.showAlertDialog()
        }
        getView()?.initPager(bluetoothAdapter)
    }

    private fun showAlert(state: Int) {
        isAlertShow = true
        when (state) {
            STATE_STRESS -> getView()?.showAlert(AlertBuilder.getStressAlert())
            STATE_SLEEP -> getView()?.showAlert(AlertBuilder.getSleepingAlert())
        }
    }

    fun onDataReceived(value: Int, state: Int) {
        val test = sdf.format(Calendar.getInstance().time)
        dataList.add(SaveModel(test, value))
        checkData(state)
    }

    private fun checkData(state: Int) {
        if (dataList.isEmpty() || dataList.size < 60) {
            return
        }

        if (dataList[dataList.size - 60].data >= dataList[dataList.size - 1].data + 60 && !isAlertShow) {
            showAlert(STATE_STRESS)
        }

        if (dataList[dataList.size - 60].data >= dataList[dataList.size - 1].data + 60 && !isAlertShow) {
            showAlert(STATE_STRESS)
        }

        if (dataList[dataList.size - 60].data <= dataList[dataList.size - 1].data - 60 && state != STATE_STRESS) {
            showAlert(STATE_STRESS)
        }

        if (dataList[dataList.size - 60].data >= dataList[dataList.size - 1].data + 20 && state != STATE_STRESS) {
            getView()?.setStatus(STATE_STRESS)
            return
        }

        if (dataList[dataList.size - 60].data <= dataList[dataList.size - 1].data - 20 && state != STATE_SLEEP) {
            getView()?.setStatus(STATE_SLEEP)
            return
        }

        if (state != STATE_STABLE) {
            getView()?.setStatus(STATE_STABLE)
        }
    }

    fun onDestroy() {
        if (dataList.isNotEmpty()) {
            getView()?.saveToFile(sdf.format(Calendar.getInstance().time), dataList)
        }
    }

    fun onAlertClick() {
        getView()?.hideAlert()
        isAlertShow = false
    }
}