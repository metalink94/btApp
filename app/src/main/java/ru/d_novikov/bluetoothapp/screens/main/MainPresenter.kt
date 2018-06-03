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


    private val STANDART_HIGHT = 0
    private val STANDART_SMALL = 0
    private var stressState: Int = 0
    private var sleepState: Int = 0
    private var veryStressState: Int = 0
    private var verySleepState: Int = 0

    fun onCreate() {
        if (bluetoothAdapter == null) {
            getView()?.showAlertDialog()
        }
        getView()?.initPager(bluetoothAdapter)
    }

    private fun showAlert(state: Int) {
        when (state) {
            STATE_STRESS -> getView()?.showAlert(AlertBuilder.getStressAlert())
            STATE_SLEEP -> getView()?.showAlert(AlertBuilder.getSleepingAlert())
        }
    }

    fun onDataReceived(data: String, state: Int) {
        val test = sdf.format(Calendar.getInstance().time)
        dataList.add(SaveModel(test, data))
        checkData(data, state)
    }

    private fun checkData(data: String, state: Int) {
        val intData = data.toInt()

        if (intData >= STANDART_HIGHT + 20) {
            stressState += 1
        } else {
            stressState = 0
        }

        if (intData <= STANDART_SMALL - 20) {
            sleepState += 1
        } else {
            sleepState = 0
        }

        if (intData >= STANDART_HIGHT + 50) {
            veryStressState += 1
        } else {
            veryStressState = 0
        }

        if (intData <= STANDART_SMALL - 50) {
            verySleepState += 1
        } else {
            verySleepState = 0
        }

        if (veryStressState == 60) {
            showAlert(STATE_STRESS)
        }
        if (verySleepState == 60) {
            showAlert(STATE_SLEEP)
        }

        if (stressState == 60 && state != STATE_STRESS) {
            getView()?.setStatus(STATE_STRESS)
            return
        }
        if (sleepState == 60 && state != STATE_SLEEP) {
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
    }
}