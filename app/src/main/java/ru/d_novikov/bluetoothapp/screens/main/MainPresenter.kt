package ru.d_novikov.bluetoothapp.screens.main

import android.bluetooth.BluetoothAdapter
import android.util.Log
import ru.d_novikov.bluetoothapp.models.SaveModel
import ru.d_novikov.bluetoothapp.mvp.ViewPresenter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class MainPresenter : ViewPresenter<MainView>() {

    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

    val dataList = mutableListOf<SaveModel>()
    val sdf = SimpleDateFormat("HH:mm:ss.SSS")

    fun onCreate() {
        if (bluetoothAdapter == null) {
            getView()?.showAlertDialog()
        }
        getView()?.initPager(bluetoothAdapter)
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
}