package ru.d_novikov.bluetoothapp.screens.main

import android.bluetooth.BluetoothAdapter
import android.os.Handler
import ru.d_novikov.bluetoothapp.models.SaveModel
import ru.d_novikov.bluetoothapp.mvp.ViewPresenter
import ru.d_novikov.bluetoothapp.utils.AlertBuilder
import java.text.SimpleDateFormat
import java.util.*


class MainPresenter : ViewPresenter<MainView>() {

    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

    val dataList = mutableListOf<SaveModel>()
    val sdf = SimpleDateFormat("HH:mm:ss.SSS")

    fun onCreate() {
        if (bluetoothAdapter == null) {
            getView()?.showAlertDialog()
        }
        getView()?.initPager(bluetoothAdapter)
        showAlert(0)
    }

    private fun showAlert(state: Int) {
        when (state) {
            0 -> getView()?.showAlert(AlertBuilder.getStressAlert())
            1 -> getView()?.showAlert(AlertBuilder.getSleepingAlert())
        }
    }

    fun onDataReceived(data: String) {
        val test = sdf.format(Calendar.getInstance().time)
        dataList.add(SaveModel(test, data))
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